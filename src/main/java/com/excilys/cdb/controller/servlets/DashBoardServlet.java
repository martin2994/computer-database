package com.excilys.cdb.controller.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.ComputerService;

/**
 * Gére toutes les actions de la page dashboard: liste des computers,
 * suppression et redirection vers les pages appropriées.
 * @author martin
 *
 */
@WebServlet(asyncSupported = false, name = "DashboardServlet", urlPatterns = { "/dashboard" })
public class DashBoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Le service des computers.
     */
    @Autowired
    private ComputerService computerService;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashBoardServlet.class);

    /**
     * Emplacement de la jsp de la page dashboard.
     */
    private final String LOCATION_DASHBOARD_JSP = "/WEB-INF/pages/dashboard.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String selection = request.getParameter("selection");
        if (!StringUtils.isBlank(selection)) {
            deleteListComputer(request, selection);
        }
        int resultPerPage = getResultPerPage(request);
        int currentPage = getCurrentPage(request);
        List<Computer> page = null;
        List<ComputerDTO> pageDTO = null;
        try {
            page = getComputer(request, currentPage, resultPerPage);
        } catch (InvalidComputerException e) {
            LOGGER.debug(e.getMessage());
        }
        pageDTO = page.stream().map(computers -> DTOMapper.fromComputer(computers)).collect(Collectors.toList());
        request.setAttribute("page", pageDTO);
        this.getServletContext().getRequestDispatcher(LOCATION_DASHBOARD_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Supprime la liste de computers sélectionnés.
     * @param request
     *            la requete en cours
     * @param selection
     *            la liste des id de computers
     */
    private void deleteListComputer(HttpServletRequest request, String selection) {
        computerService.deleteComputerList(ComputerMapper.convertListId(selection));
    }

    /**
     * Permet de récupérer la nombre d'élement par page.
     * @param request
     *            la requete en cours
     * @return la nombre d'élément par page
     */
    private int getResultPerPage(HttpServletRequest request) {
        String resultPerPageString = request.getParameter("resultPerPage");
        int resultPerPage = 10;
        if (!StringUtils.isBlank(resultPerPageString) && StringUtils.isNumeric(resultPerPageString)) {
            resultPerPage = Integer.parseInt(resultPerPageString);
        }
        request.setAttribute("resultPerPage", resultPerPage);
        return resultPerPage;
    }

    /**
     * Permet de récupérer la page courante.
     * @param request
     *            la requete en cours
     * @return la page courante
     */
    private int getCurrentPage(HttpServletRequest request) {
        String currentPageString = request.getParameter("page");
        int currentPage = 1;
        if (!StringUtils.isBlank(currentPageString)  && StringUtils.isNumeric(currentPageString)) {
            currentPage = Integer.parseInt(currentPageString);
        }
        return currentPage;
    }

    /**
     * Permet de récupérer les computers à afficher.
     * @param request
     *            la requete en cours
     * @param currentPage
     *            la page courante
     * @param resultPerPage
     *            le nombre d'élement à afficher
     * @return la page avec les computers
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    private List<Computer> getComputer(HttpServletRequest request, int currentPage, int resultPerPage) throws InvalidComputerException {
        String buttonTest = request.getParameter("buttonTest");
        String search = request.getParameter("search");
        List<Computer> computers = new ArrayList<>();
        if (!StringUtils.isBlank(buttonTest)) {
            if ("Filter by name".equals(buttonTest)) {
                currentPage = 1;
                return getComputerByNamePerPage(request, search, currentPage, resultPerPage);
            }
        } else {
            if (StringUtils.isBlank(search)) {
                computers = getComputerPerPage(request, currentPage, resultPerPage);
            } else {
                computers = getComputerByNamePerPage(request, search, currentPage, resultPerPage);
            }
        }
        return computers;
    }

    /**
     * Permet de récupérer les computers d'une page.
     * @param request
     *            la requete en cours
     * @param currentPage
     *            la page courante à afficher
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la page de computer
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    private List<Computer> getComputerPerPage(HttpServletRequest request, int currentPage, int resultPerPage) throws InvalidComputerException {
        int numberComputer = computerService.getCountComputers();
        double numberPage = (double) numberComputer / (double) resultPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        request.getSession().setAttribute("currentPage", currentPage);
        request.setAttribute("nbComputers", numberComputer);
        return computerService.getComputers(currentPage - 1, resultPerPage).getResults();
    }

    /**
     * Permet de récupérer la page de computers avec une nom donné .
     * @param request
     *            la requete en cours
     * @param search
     *            le nom à rechercher
     * @param currentPage
     *            la page courante à afficher
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la page de computer recherchée
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    private List<Computer> getComputerByNamePerPage(HttpServletRequest request, String search, int currentPage,
            int resultPerPage) throws InvalidComputerException {
        int numberComputer = computerService.getCountComputersByName(search);
        double numberPage = (double) numberComputer / (double) resultPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        request.getSession().setAttribute("currentPage", currentPage);
        request.setAttribute("searchParam", search);
        request.setAttribute("nbComputers", numberComputer);
        return computerService.getComputersByName(search, currentPage - 1, resultPerPage).getResults();
    }

}
