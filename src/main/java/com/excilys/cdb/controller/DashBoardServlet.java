package com.excilys.cdb.controller;

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
        int numberComputer = facade.getCountComputers();
        List<Computer> page = facade.getComputers(0, resultPerPage).getResults();
        List<ComputerDTO> pageDTO = page.stream().map(computers -> DTOMapper.convertComputerToComputerDTO(computers))
                .collect(Collectors.toList());
        request.setAttribute("nbComputers", numberComputer);
        request.setAttribute("page", pageDTO);
        this.getServletContext().getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
