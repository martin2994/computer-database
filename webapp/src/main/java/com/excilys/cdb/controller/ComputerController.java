package com.excilys.cdb.controller;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
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

    private MessageSource messageSource;

    /**
     * Le service des companies.
     */
    private CompanyService companyService;

    /**
     * Valeur par défaut.
     */
    private static final String RESULTS_PER_PAGE = "10";
    private static final String CURRENT_PAGE = "1";
    private static final String SEARCH = "";

    /**
     * redirection jsp.
     */
    private static final String DASHBOARD_JSP = "dashboard";
    private static final String ADDCOMPUTER_JSP = "addComputer";
    private static final String EDITCOMPUTER_JSP = "editComputer";
    private static final String ERROR_404_JSP = "404";
    private static final String REDIRECT_DASHBOARD = "redirect:/computer";

    /**
     * Attributs des jsp.
     */
    private static final String ATTRIBUT_ERREUR = "erreur";
    private static final String ATTRIBUT_MESSAGE = "message";
    private static final String ATTRIBUT_NBCOMPUTERS = "nbComputers";
    private static final String ATTRIBUT_RESULT_PER_PAGE = "resultPerPage";
    private static final String ATTRIBUT_CURRENT_PAGE = "currentPage";
    private static final String ATTRIBUT_PAGE_COMPUTERS = "page";
    private static final String ATTRIBUT_INFO_COMPUTER = "computer";
    private static final String ATTRIBUT_LIST_COMPANIES = "companies";
    private static final String ATTRIBUT_SEARCH = "searchParam";

    /**
     * Paramètres.
     */
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_RESULT_PER_PAGE = "resultPerPage";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SELECTION = "selection";
    private static final String PARAM_COMPUTER = "computer";
    private static final String PARAM_ID = "id";

    /**
     * Mapping.
     */
    private static final String MAPPING_ADD = "/add";
    private static final String MAPPING_DELETE = "/delete";
    private static final String MAPPING_EDIT = "/{" + PARAM_ID + "}";

    /**
     * Textes internationalisés.
     */
    private static final String TEXT_ERROR_DATE = "java.text.error.date";
    private static final String TEXT_CREATED = "java.text.created";
    private static final String TEXT_UPDATED = "java.text.updated";

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
     * @param messageSource
     *            les messages internationalisés
     */
    public ComputerController(ComputerService computerService, CompanyService companyService,
            MessageSource messageSource) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.messageSource = messageSource;
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
    public String computerPage(
            @RequestParam(value = PARAM_SEARCH, required = false, defaultValue = SEARCH) String search,
            @RequestParam(value = PARAM_PAGE, required = false, defaultValue = CURRENT_PAGE) int nbPage,
            @RequestParam(value = PARAM_RESULT_PER_PAGE, required = false, defaultValue = RESULTS_PER_PAGE) int resultPerPage,
            ModelMap model) {
        List<Computer> page = new ArrayList<>();
        List<ComputerDTO> pageDTO = new ArrayList<>();
        int count = 0;
        try {
            int currentPage = nbPage;
            if (StringUtils.isBlank(search)) {
                count = computerService.getCountComputers();
                currentPage = getCurrentPage(currentPage, count, resultPerPage);
                page = computerService.getComputers(currentPage - 1, resultPerPage).getResults();
            } else {
                count = computerService.getCountComputersByName(search);
                currentPage = getCurrentPage(currentPage, count, resultPerPage);
                page = computerService.getComputersByName(search, currentPage - 1, resultPerPage).getResults();
            }
        } catch (InvalidComputerException e) {
            LOGGER.debug(e.getMessage());
        }
        pageDTO = page.stream().map(computers -> DTOMapper.fromComputer(computers)).collect(Collectors.toList());
        model.addAttribute(ATTRIBUT_PAGE_COMPUTERS, pageDTO);
        model.addAttribute(ATTRIBUT_SEARCH, search);
        model.addAttribute(ATTRIBUT_CURRENT_PAGE, nbPage);
        model.addAttribute(ATTRIBUT_RESULT_PER_PAGE, resultPerPage);
        model.addAttribute(ATTRIBUT_NBCOMPUTERS, count);
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
    @PostMapping(value = MAPPING_DELETE)
    public String deleteComputers(@RequestParam(value = PARAM_SELECTION, required = true) Set<Long> selection,
            ModelMap model) {
        computerService.deleteComputerList(selection);
        return REDIRECT_DASHBOARD;
    }

    /**
     * Affiche la page AddComputer.
     * @param model
     *            le modèle
     * @return la jsp affichée
     */
    @GetMapping(value = MAPPING_ADD)
    public String addComputerPage(ModelMap model) {
        List<Company> companies = companyService.getCompanies();
        model.addAttribute(ATTRIBUT_LIST_COMPANIES, companies);
        model.addAttribute(ATTRIBUT_INFO_COMPUTER, new ComputerDTO());
        return ADDCOMPUTER_JSP;
    }

    /**
     * Permet de créer un computer.
     * @param computerDTO
     *            Le dto du formulaire
     * @param model
     *            le modèle
     * @param bindingResult
     *            bind le dto
     * @param locale
     *            le language local
     * @return la jsp affichée
     */
    @PostMapping(value = MAPPING_ADD)
    public String createComputer(@ModelAttribute(PARAM_COMPUTER) @Valid ComputerDTO computerDTO,
            BindingResult bindingResult, ModelMap model, Locale locale) {
        String erreur = "", message = "";
        if (bindingResult.hasErrors()) {
            erreur = bindingResult.getAllErrors().toString();
            return ADDCOMPUTER_JSP;
        } else {
            try {
                Computer computer = DTOMapper.toComputer(computerDTO);
                long id = computerService.createComputer(computer);
                message = messageSource.getMessage(TEXT_CREATED, new Object[] {id }, locale);
                model.addAttribute(ATTRIBUT_MESSAGE, message);
            } catch (InvalidComputerException | InvalidCompanyException e) {
                erreur = e.getMessage();
            } catch (DateTimeException e) {
                message = messageSource.getMessage(TEXT_ERROR_DATE, null, locale);
                erreur = message;
            }
        }
        List<Company> companies = companyService.getCompanies();
        model.addAttribute(ATTRIBUT_LIST_COMPANIES, companies);
        model.addAttribute(ATTRIBUT_ERREUR, erreur);
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
    @GetMapping(value = MAPPING_EDIT)
    public String editComputerPage(@PathVariable(PARAM_ID) Long id, ModelMap model) {
        String jsp = EDITCOMPUTER_JSP;
        List<Company> companies = companyService.getCompanies();
        model.addAttribute(ATTRIBUT_LIST_COMPANIES, companies);
        try {
            ComputerDTO computerDTO = DTOMapper.fromComputer(computerService.getComputerDetails(id));
            model.addAttribute(ATTRIBUT_INFO_COMPUTER, computerDTO);
        } catch (NoObjectException | InvalidComputerException e) {
            LOGGER.debug(e.getMessage());
            jsp = ERROR_404_JSP;
        }
        return jsp;
    }

    /**
     * Permet de modifier un computer.
     * @param computerDTO
     *            le dto du formulaire
     * @param model
     *            le modèle
     * @param bindingResult
     *            bind le dto
     * @param locale
     *            le language local
     * @return la jsp affichée
     */
    @PostMapping(value = MAPPING_EDIT)
    public String editComputer(@ModelAttribute(PARAM_COMPUTER) @Valid ComputerDTO computerDTO,
            BindingResult bindingResult, ModelMap model, Locale locale) {
        String erreur = "", message = "";
        if (bindingResult.hasErrors()) {
            erreur = bindingResult.getAllErrors().toString();
        } else {
            try {
                Computer computer = DTOMapper.toComputer(computerDTO);
                computerDTO = DTOMapper.fromComputer(computerService.updateComputer(computer));
                message = messageSource.getMessage(TEXT_UPDATED, null, locale);
                model.addAttribute(ATTRIBUT_MESSAGE, message);
            } catch (InvalidComputerException | InvalidCompanyException e) {
                erreur = e.getMessage();
            } catch (DateTimeException e) {
                message = messageSource.getMessage(TEXT_ERROR_DATE, null, locale);
                erreur = message;
            }
        }
        model.addAttribute(ATTRIBUT_ERREUR, erreur);
        return editComputerPage(computerDTO.getId(), model);
    }

    /**
     * Permet de calculer la page courante.
     * @param currentPage
     *            la page voulue
     * @param count
     *            le nombre de computers
     * @param resultPerPage
     *            le nombre de computers par page
     * @return la page visible
     */
    private int getCurrentPage(int currentPage, int count, int resultPerPage) {
        int maxPage = (int) Math.ceil((double) count / (double) resultPerPage);
        if (currentPage > maxPage) {
            currentPage = maxPage;
        }
        return currentPage;
    }

}
