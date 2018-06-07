package com.excilys.cdb.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.services.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {

	private CompanyService companyService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping(params= {"page", "resultPerPage"})
	public ResponseEntity<Collection<Company>> getCompanieSPage(@RequestParam(name = "page", required = true) int page,
			@RequestParam(name = "resultPerPage", required = true) int resultPerPage) {
		try {
			List<Company> companies = companyService.getCompanies(page, resultPerPage).getResults();
			return new ResponseEntity<>(companies, HttpStatus.OK);
		} catch (InvalidCompanyException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}

	@GetMapping
	public ResponseEntity<Collection<Company>> getListCompanies() {
		List<Company> companies = companyService.getCompanies();
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {
		Company company;
		try {
			company = companyService.getCompany(id);
			LOGGER.debug(company.toString());
			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (InvalidCompanyException | NoObjectException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}

	@PostMapping
	public ResponseEntity<Void> addCompany(@RequestBody Company company) {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Company> updateCompany(@PathVariable("id") Long id, @RequestBody Company company) {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
		try {
			if (companyService.deleteCompany(id)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (InvalidCompanyException e) {
			LOGGER.debug(e.toString());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}

}
