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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.services.ComputerService;

@Controller
@RequestMapping("/computer")
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
    private static final String ADDCOMPUTER_JSP = "addComputer";
    private static final String EDITCOMPUTER_JSP = "editComputer";

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    /**
     * Injection du service.
     * @param computerService
     *            Le service à injecter
     * @param companyService
     *            Le service à injecter
     */
    @Autowired
    public ComputerController(ComputerService computerService, CompanyService companyService) {
        this.computerService = computerService;
        this.companyService = companyService;
    }

    /**
     * Permet d'afficher la page voulue avec la liste des computers recherchés.
     * @param search
     *            La recherche
     * @param nbPage
     *            la page voulue
     * @param resultPerPage
     *            ke nombre de resultat par page
     * @param model
     *            le modele
     * @return la jsp affichée
     */
    @GetMapping("")
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
     * @param selection
     *            la liste des ids
     * @param model
     *            le modèle
     * @return la jsp affichée
     */
    @PostMapping(value = "/delete")
    public String deleteComputers(@RequestBody String selection, ModelMap model) {
        try {
            LOGGER.debug(selection);
            computerService.deleteComputerList(ComputerMapper.convertListId(selection));
        } catch (InvalidIdException e) {
            LOGGER.debug(e.getMessage());
        }
        return "redirect:/computer";
    }

    /**
     * Affiche la page AddComputer.
     * @param model
     *            le modèle
     * @return la jsp affichée
     */
    @GetMapping(value = "/add")
    public String addComputerPage(ModelMap model) {
        List<Company> companies = companyService.getCompanies();
        model.addAttribute("companies", companies);
        return ADDCOMPUTER_JSP;
    }

    /**
     * Permet de créer un computer.
     * @return la jsp affichée
     */
    @PostMapping(value = "add")
    public String createComputer() {
        return ADDCOMPUTER_JSP;
    }

    /**
     * Affiche la page EditComputer.
     * @param id
     *            l'id du computer à afficher
     * @param model
     *            le modèle
     * @return la jsp affichée
     */
    @GetMapping(value = "/{id}")
    public String editComputerPage(@PathVariable("id") Integer id, ModelMap model) {
        List<Company> companies = companyService.getCompanies();
        model.addAttribute("companies", companies);
        try {
            ComputerDTO computerDTO = DTOMapper.fromComputer(computerService.getComputerDetails(id));
            model.addAttribute("computer", computerDTO);
        } catch (NoObjectException | InvalidComputerException e) {
            LOGGER.debug(e.getMessage());
        }
        return EDITCOMPUTER_JSP;
    }

    /**
     * Permet de modifier un computer.
     * @return la jsp affichée
     */
    @PostMapping(value = "/{id}")
    public String editComputer() {
        return EDITCOMPUTER_JSP;
    }

}
