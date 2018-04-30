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

import com.excilys.cdb.exceptions.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.Facade;

/**
 * Servlet implementation class AddComputerServlet.
 */
@WebServlet(asyncSupported = false, name = "AddComputerServlet", urlPatterns = { "/addComputer" })
public class AddComputerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Facade facade;

    /**
     * Constructeur vide.
     */
    public AddComputerServlet() {
        super();
        facade = Facade.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Company> companies = facade.getCompanies();
        String buttonTest = request.getParameter("buttonTest");
        try {
            if (buttonTest != null) {
                if (buttonTest.equals("Add")) {
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
                    if (companyId != null && !companyId.equals("0")) {
                        company = companies.stream()
                                .filter(currentCompany -> currentCompany.getId() == Integer.parseInt(companyId))
                                .findFirst().orElse(null);
                    }
                    Computer computer = new Computer.Builder(name).introduced(introducedDate)
                            .discontinued(discontinuedDate).manufacturer(company).build();
                    long idNewComputer = facade.createComputer(computer);
                    if (idNewComputer == 0) {
                        request.setAttribute("erreur", "Computer not created.");
                    } else {
                        request.setAttribute("message", "Computer created.");
                    }
                }
            }
        } catch (InvalidComputerException e) {
            request.setAttribute("erreur", e.getMessage());
        } catch (DateTimeParseException e) {
            request.setAttribute("erreur", "Invalid date format.");
        }
        request.setAttribute("companies", companies);
        this.getServletContext().getRequestDispatcher("/WEB-INF/pages/addComputer.jsp").forward(request, response);
    }

}
