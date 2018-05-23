package com.excilys.cdb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.services.ComputerService;

@Controller
@RequestMapping({"/computer", "/"})
public class ComputerController {

    /**
     * Le service des computers.
     */
    private ComputerService computerService;

    /**
     * Le service des companies.
     */
    private CompanyService companyService;

    private static final String RESULTS_PER_PAGE = "10";
    private static final String CURRENT_PAGE = "1";
    private static final String SEARCH = "";
    private static final String DASHBOARD_JSP = "dashboard";

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    /**
     * Injection du service.
     * @param computerService
     *            Le service à injecter
     * @param companyService
     *              Le service à injecter
     */
    @Autowired
    public ComputerController(ComputerService computerService, CompanyService companyService) {
        this.computerService = computerService;
        this.companyService = companyService;
    }

    /**
     * Permet d'afficher la page voulue avec la liste des computers recherchés.
     * @param search La recherche
     * @param nbPage la page voulue
     * @param resultPerPage ke nombre de resultat par page
     * @param model le modele
     * @return la jsp dashboard
     */
    @GetMapping
    public String computerPage(@RequestParam(value = "search", required = false, defaultValue = SEARCH) String search,
            @RequestParam(value = "page", required = false, defaultValue = CURRENT_PAGE) int nbPage,
            @RequestParam(value = "resultPerPage", required = false, defaultValue = RESULTS_PER_PAGE) int resultPerPage,
            ModelMap model) {
        List<Computer> page = null;
        List<ComputerDTO> pageDTO = null;
        int count = 0;
        try {
            if (StringUtils.isBlank(search)) {
                count = computerService.getCountComputers();
                page = computerService.getComputers(nbPage - 1, resultPerPage).getResults();
            } else {
                count = computerService.getCountComputersByName(search);
                page = computerService.getComputersByName(search, nbPage - 1, resultPerPage).getResults();
            }
        } catch (InvalidComputerException e) {
            LOGGER.debug(e.getMessage());
        }
        pageDTO = page.stream().map(computers -> DTOMapper.fromComputer(computers)).collect(Collectors.toList());
        model.addAttribute("page", pageDTO);
        model.addAttribute("searchParam", search);
        model.addAttribute("currentPage", nbPage);
        model.addAttribute("resultPerPage", resultPerPage);
        model.addAttribute("nbComputers", count);
        return DASHBOARD_JSP;
    }

    /**
     * Permet de supprimer une liste de computers.
     * @param selection la liste des ids
     * @param model le modèle
     * @return la jsp
     */
    @PostMapping(value = "/delete")
    public String deleteComputers(@RequestBody String selection, ModelMap model) {
        try {
            LOGGER.debug(selection);
            computerService.deleteComputerList(ComputerMapper.convertListId(selection));
        } catch (InvalidIdException e) {
            LOGGER.debug(e.getMessage());
        }
        return computerPage(SEARCH, Integer.parseInt(CURRENT_PAGE), Integer.parseInt(RESULTS_PER_PAGE), model);
    }
}
