package com.excilys.cdb.controller.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.services.ComputerService;

/**
 * Servlet implementation class AddComputerServlet. Gére toutes les actions de
 * l'ajout d'un computer.
 */
@WebServlet(asyncSupported = false, name = "AddComputerServlet", urlPatterns = { "/addComputer" })
public class AddComputerServlet extends HttpServlet {

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
     * Emplacement de la jsp de la page Add.
     */
    private final String LOCATION_ADD_JSP = "/WEB-INF/pages/addComputer.jsp";

    /**
     * Constructeur vide.
     */
    public AddComputerServlet() {
        super();
        computerService = ComputerService.getInstance();
        companyService = CompanyService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Company> companies = companyService.getCompanies();
        String buttonTest = request.getParameter("buttonTest");
        String message = null, erreur = null;
        try {
            if (!StringUtils.isBlank(buttonTest)) {
                if ("Add".equals(buttonTest)) {
                    long idNewComputer = createComputer(request);
                    if (idNewComputer == 0) {
                        erreur = "Computer not created.";
                    } else {
                        message = "Computer created.";
                    }
                }
            }
        } catch (InvalidComputerException | InvalidCompanyException e) {
            erreur = e.getMessage();
        } catch (DateTimeParseException e) {
            erreur = "Invalid date format.";
        }
        request.setAttribute("message", message);
        request.setAttribute("erreur", erreur);
        request.setAttribute("companies", companies);
        this.getServletContext().getRequestDispatcher(LOCATION_ADD_JSP).forward(request, response);
    }

    /**
     * Vérifie et crée le computer à partir des infos données.
     * @param request
     *            La requete en cours
     * @return L'id du computer créé
     * @throws InvalidCompanyException
     *             Exception lancée quand la company choisie est invalide
     * @throws NumberFormatException
     *             Exception lancée quand on entre un id qui n'est pas un nombre
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalide
     */
    private long createComputer(HttpServletRequest request)
            throws NumberFormatException, InvalidCompanyException, InvalidComputerException {
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
        if (companyId != null && !"0".equals(companyId)) {
            company = companyService.getCompany(Long.parseLong(companyId));
        }
        Computer computer = new Computer.Builder(name).introduced(introducedDate).discontinued(discontinuedDate)
                .manufacturer(company).build();
        return computerService.createComputer(computer);
    }

}
