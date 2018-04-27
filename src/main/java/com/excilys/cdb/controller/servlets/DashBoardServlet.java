package com.excilys.cdb.controller.servlets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.Facade;

@WebServlet(asyncSupported = false, name = "DashboardServlet", urlPatterns = { "/dashboard" })
public class DashBoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Facade facade;

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
        Integer currentPage = (Integer) request.getSession().getAttribute("currentPage");
        if (currentPage == null) {
            currentPage = 1;
        } else {
            String page = request.getParameter("page");
            if (page != null) {
                currentPage = Integer.parseInt(page);
            }
        }
        int numberComputer = facade.getCountComputers();
        double numberPage = (double) numberComputer / (double) resultPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        request.getSession().setAttribute("currentPage", currentPage);
        List<Computer> page = facade.getComputers(currentPage - 1, resultPerPage).getResults();
        List<ComputerDTO> pageDTO = page.stream().map(computers -> DTOMapper.convertComputerToComputerDTO(computers))
                .collect(Collectors.toList());
        request.setAttribute("nbComputers", numberComputer);
        request.setAttribute("page", pageDTO);
        String action = request.getParameter("todo");
        if (action != null) {
            switch (action) {
            case "":
                break;
            default:
                break;
            }
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
