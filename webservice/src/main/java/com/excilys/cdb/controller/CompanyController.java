package com.excilys.cdb.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;

import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.utils.Page;

@RestController
@RequestMapping("/company")
public class CompanyController {

	private CompanyService companyService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping(path = "/{id}/computers", params = { "page", "resultPerPage" })
	public ResponseEntity<Collection<Computer>> getComputersByCompanyIdPage(@PathVariable("id") Long id, @RequestParam(name = "page", required = true) int page,
			@RequestParam(name = "resultPerPage", required = true) int resultPerPage) throws InvalidComputerException {
		List<Computer> computers = companyService.getComputersByCompanyId(id, page, resultPerPage).getResults();
		return new ResponseEntity<>(computers, HttpStatus.OK);
	}
	
	@GetMapping(params = { "page", "resultPerPage" })
	public ResponseEntity<Page<Company>> getCompaniesPage(@RequestParam(name = "page", required = true) int page,
			@RequestParam(name = "resultPerPage", required = true) int resultPerPage) throws InvalidCompanyException {
		Page<Company> companies = companyService.getCompanies(page, resultPerPage);
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}
	
//	@GetMapping(params = { "page", "resultPerPage" , "search"})
	@RequestMapping(params = { "page","resultPerPage", "search" }, method = RequestMethod.GET )
	public ResponseEntity<Page<Company>> getCompanieSPageByName(@RequestParam(name = "page", required = true) int page,
			@RequestParam(name = "resultPerPage", required = true) int resultPerPage,@RequestParam(name = "search", required = true) String search) throws Exception {
		Page<Company> companies = companyService.getCompaniesByName(page, resultPerPage, search);
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Collection<Company>> getListCompanies() {
		List<Company> companies = companyService.getCompanies();
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompany(@PathVariable("id") Long id)
			throws InvalidCompanyException, NoObjectException, InvalidIdException {
		Company company;
		company = companyService.getCompany(id);
		LOGGER.debug(company.toString());
		return new ResponseEntity<>(company, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/computers")
	public ResponseEntity<Collection<Computer>> getComputersByCompanyId(@PathVariable("id") Long id)
			throws InvalidCompanyException, NoObjectException, InvalidIdException {
		List<Computer> computers;
		computers = companyService.getComputersByCompanyId(id);
		LOGGER.debug(computers.toString());
		return new ResponseEntity<>(computers, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Void> addCompany(@RequestBody Company company, UriComponentsBuilder ucb)
			throws InvalidCompanyException, NoObjectException {
		long idNewCompany = companyService.createCompany(company);
		if (idNewCompany > 0) {
			UriComponents uriComponents = ucb.path("/{id}").buildAndExpand(idNewCompany);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();

	}

	@PutMapping("/{id}")
	public ResponseEntity<Company> updateCompany(@PathVariable("id") Long id, @RequestBody Company company)
			throws InvalidCompanyException, NoObjectException, InvalidIdException {
		if(id != company.getId()) {
			throw new InvalidIdException(ExceptionMessage.INVALID_ID.getMessage());
		}
		Company companyUpdate = companyService.updateCompany(company);
		return new ResponseEntity<>(companyUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) throws InvalidCompanyException {
		if (companyService.deleteCompany(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/count")
	public ResponseEntity<Integer> countCompanies() {
		return new ResponseEntity<>(companyService.getCountCompanies(), HttpStatus.OK);
	}

}
