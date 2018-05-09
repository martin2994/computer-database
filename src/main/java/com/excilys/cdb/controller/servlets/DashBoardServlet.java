package com.excilys.cdb.controller.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.Facade;

@WebServlet(asyncSupported = false, name = "DashboardServlet", urlPatterns = { "/dashboard" })
public class DashBoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Facade facade;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashBoardServlet.class);

    /**
     * Constructeur vide.
     */
    public DashBoardServlet() {
        super();
        facade = Facade.getInstance();
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
        page = getComputer(request, currentPage, resultPerPage);
        pageDTO = page.stream().map(computers -> DTOMapper.convertComputerToComputerDTO(computers))
                .collect(Collectors.toList());
        request.setAttribute("page", pageDTO);
        this.getServletContext().getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response);
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
        Set<Long> idComputer = Arrays.stream(selection.split(",")).map(stringId -> Long.parseLong(stringId))
                .collect(Collectors.toSet());
        facade.deleteComputerList(idComputer.toString().replace("[", "(").replace("]", ")"));
    }

    /**
     * Permet de récupérer la nombre d'élement par page.
     * @param request
     *            la requete en cours
     * @return la nombre d'élément par page
     */
    private int getResultPerPage(HttpServletRequest request) {
        Integer resultPerPage = (Integer) request.getSession().getAttribute("resultPerPage");
        if (resultPerPage == null) {
            resultPerPage = 10;
        } else {
            String result = request.getParameter("resultPerPage");
            if (result != null) {
                resultPerPage = Integer.parseInt(result);
            }
        }
        request.getSession().setAttribute("resultPerPage", resultPerPage);
        return resultPerPage;
    }

    /**
     * Permet de récupérer la page courante.
     * @param request
     *            la requete en cours
     * @return la page courante
     */
    private int getCurrentPage(HttpServletRequest request) {
        Integer currentPage = (Integer) request.getSession().getAttribute("currentPage");
        if (currentPage == null) {
            currentPage = 1;
        } else {
            String page = request.getParameter("page");
            if (page != null) {
                currentPage = Integer.parseInt(page);
            }
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
     */
    private List<Computer> getComputer(HttpServletRequest request, int currentPage, int resultPerPage) {
        String buttonTest = request.getParameter("buttonTest");
        String search = request.getParameter("search");
        if (!StringUtils.isBlank(buttonTest)) {
            if ("Filter by name".equals(buttonTest)) {
                currentPage = 1;
                return getComputerByNamePerPage(request, search, currentPage, resultPerPage);
            }
        } else {
            if (StringUtils.isBlank(search)) {
                return getComputerPerPage(request, currentPage, resultPerPage);
            } else {
                return getComputerByNamePerPage(request, search, currentPage, resultPerPage);
            }
        }
        return new ArrayList<>();
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
     */
    private List<Computer> getComputerPerPage(HttpServletRequest request, int currentPage, int resultPerPage) {
        int numberComputer = facade.getCountComputers();
        double numberPage = (double) numberComputer / (double) resultPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        request.getSession().setAttribute("currentPage", currentPage);
        request.setAttribute("nbComputers", numberComputer);
        return facade.getComputers(currentPage - 1, resultPerPage).getResults();
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
     */
    private List<Computer> getComputerByNamePerPage(HttpServletRequest request, String search, int currentPage,
            int resultPerPage) {
        int numberComputer = facade.getCountComputersByName(search);
        double numberPage = (double) numberComputer / (double) resultPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        request.getSession().setAttribute("currentPage", currentPage);
        request.setAttribute("searchParam", search);
        request.setAttribute("nbComputers", numberComputer);
        return facade.getComputersByName(search, currentPage - 1, resultPerPage).getResults();
    }

}
