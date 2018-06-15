package com.excilys.cdb.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.ComputerService;
import com.excilys.cdb.utils.Page;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/computer")
public class ComputerController {

	private ComputerService computerService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

	public ComputerController(ComputerService computerService) {
		this.computerService = computerService;
	}

	@GetMapping
	public ResponseEntity<Collection<ComputerDTO>> getListComputer(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "resultPerPage", defaultValue = "10") int resultPerPage)
			throws InvalidComputerException {
		Page<Computer> pageComputer = computerService.getComputers(page, resultPerPage);
		List<ComputerDTO> list_computers = pageComputer.getResults().stream()
				.map(computers -> DTOMapper.fromComputer(computers)).collect(Collectors.toList());
		return new ResponseEntity<>(list_computers, HttpStatus.OK);
	}

	@GetMapping(params = "search")
	public ResponseEntity<Collection<ComputerDTO>> getListComputer(
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "resultPerPage", defaultValue = "10") int resultPerPage)
			throws InvalidComputerException {
		Page<Computer> pageComputer = computerService.getComputersByName(search, page, resultPerPage);
		List<ComputerDTO> list_computers = pageComputer.getResults().stream()
				.map(computers -> DTOMapper.fromComputer(computers)).collect(Collectors.toList());
		return new ResponseEntity<>(list_computers, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ComputerDTO> getComputer(@PathVariable("id") Long id)
			throws NoObjectException, InvalidIdException {
		ComputerDTO computerDTO = DTOMapper.fromComputer(computerService.getComputerDetails(id));
		return new ResponseEntity<>(computerDTO, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Void> addComputer(@RequestBody ComputerDTO computerDTO, UriComponentsBuilder ucb)
			throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
		Computer computer = DTOMapper.toComputer(computerDTO);
		long idNewComputer = computerService.createComputer(computer);
		if (idNewComputer > 0) {
			UriComponents uriComponents = ucb.path("/{id}").buildAndExpand(idNewComputer);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<ComputerDTO> updateComputer(@PathVariable("id") Long id, @RequestBody ComputerDTO computerDTO)
			throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
		if (id != computerDTO.getId()) {
			throw new InvalidIdException(ExceptionMessage.INVALID_ID.getMessage());
		}
		Computer computer = DTOMapper.toComputer(computerDTO);
		ComputerDTO computerUpdate = DTOMapper.fromComputer(computerService.updateComputer(computer));
		return new ResponseEntity<>(computerUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComputer(@PathVariable("id") Long id) throws InvalidIdException {
		if (computerService.deleteComputer(id)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.noContent().build();
		}

	}

	@DeleteMapping
	public ResponseEntity<Void> deleteComputers(@RequestBody Collection<Long> idList) {
		Set<Long> ids = idList.stream().collect(Collectors.toSet());
		if (computerService.deleteComputerList(ids)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping(value = "/count", params = "search")
	public ResponseEntity<Integer> countComputerSearch(@RequestParam(name = "search") String search) {
		return new ResponseEntity<>(computerService.getCountComputersByName(search), HttpStatus.OK);
	}

	@GetMapping("/count")
	public ResponseEntity<Integer> countComputer() {
		return new ResponseEntity<>(computerService.getCountComputers(), HttpStatus.OK);
	}

}
