package com.excilys.cdb.controller.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.services.ComputerService;

/**
 * Servlet implementation class EditComputerServlet. Gére toutes les actions de
 * l'édition d'un computer.
 */
@WebServlet(asyncSupported = false, name = "EditComputerServlet", urlPatterns = { "/editComputer" })
public class EditComputerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Le service des computers.
     */
    private ComputerService computerService;

    /**
     * Le service des companies.
     */
    private CompanyService companyService;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerServlet.class);

    /**
     * Emplacement de la jsp de la page d'édition.
     */
    private final String LOCATION_EDIT_JSP = "/WEB-INF/pages/editComputer.jsp";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerServlet() {
        super();
        computerService = ComputerService.getInstance();
        companyService = CompanyService.getInstance();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String buttonTest = request.getParameter("buttonTest");
        ComputerDTO computerDTO = null;
        String error = null, message = null;
        try {
            if (!StringUtils.isBlank(buttonTest)) {
                if ("Edit".equals(buttonTest)) {
                    computerDTO = updateComputer(request);
                    message = "Update done.";
                }
            } else {
                computerDTO = DTOMapper.convertComputerToComputerDTO(
                        computerService.getComputerDetails(Long.parseLong(request.getParameter("id"))));
            }
        } catch (NumberFormatException | InvalidComputerException | InvalidCompanyException e) {
            LOGGER.warn("ERROR UPDATE " + e.getMessage());
            error = e.getMessage();
        }
        List<Company> companies = companyService.getCompanies();
        request.setAttribute("erreur", error);
        request.setAttribute("message", message);
        request.setAttribute("computer", computerDTO);
        request.setAttribute("companies", companies);
        this.getServletContext().getRequestDispatcher(LOCATION_EDIT_JSP).forward(request, response);
    }

    /**
     * Vérifie et met à jour le computer à partir des infos données.
     * @param request
     *            La requete en cours
     * @return Le nouveau computer
     * @throws InvalidCompanyException
     *             Exception lancée quand la company choisie est invalide
     * @throws NumberFormatException
     *             Exception lancée quand on entre un id qui n'est pas un nombre
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalide
     */
    private ComputerDTO updateComputer(HttpServletRequest request)
            throws NumberFormatException, InvalidCompanyException, InvalidComputerException {
        ComputerDTO computerDTO = DTOMapper.convertComputerToComputerDTO(
                computerService.getComputerDetails(Long.parseLong(request.getParameter("idComputer"))));
        String name = request.getParameter("computerName");
        String introduced = request.getParameter("introduced");
        LocalDate introducedDate = null;
        if (!introduced.isEmpty()) {
            introducedDate = LocalDate.parse(introduced);
        }
        LocalDate discontinuedDate = null;
        String discontinued = request.getParameter("discontinued");
        if (!discontinued.isEmpty()) {
            discontinuedDate = LocalDate.parse(discontinued);
        }
        String companyId = request.getParameter("companyId");
        Company company = null;
        long idCompany = Long.parseLong(companyId);
        if (companyId != null && !"0".equals(companyId)) {
            if (idCompany != computerDTO.getManufacturerId()) {
                company = companyService.getCompany(idCompany);
            } else {
                company = new Company(idCompany, computerDTO.getManufacturer());
            }
        }
        Computer computer = new Computer.Builder(name).id(computerDTO.getId()).introduced(introducedDate)
                .discontinued(discontinuedDate).manufacturer(company).build();
        return DTOMapper.convertComputerToComputerDTO(computerService.updateComputer(computer));
    }

}
