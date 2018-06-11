package com.excilys.cdb.clients;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.model.Company;

/**
 * Classe qui permet de récupérer les infos de la BD à partir du webservice REST.
 * @author martin
 *
 */
public class RestClient {

	private static Client client = ClientBuilder.newClient();

	private static final String URI_COMPUTER = "http://localhost:8080/webservice/computer/";
	private static final String URI_COMPANY = "http://localhost:8080/webservice/company/";

	/**
	 * Permet de récupérer les infos d'un computer.
	 * @param id l'id du computer
	 * @return les infos du computer
	 */
	public static ComputerDTO getComputer(String id) {
		return client.target(URI_COMPUTER).path(id).request(MediaType.APPLICATION_JSON)
				.get(ComputerDTO.class);
	}

	/**
	 * Permet de récupérer les infos d'une company.
	 * @param id l'id de la company
	 * @return les infos de la company
	 */
	public static Company getCompany(String id) {
		return client.target(URI_COMPANY).path(id).request(MediaType.APPLICATION_JSON)
				.get(Company.class);
	}

	/**
	 * Permet de créer un computer.
	 * @param computerDTO les infos du computer à ajouter
	 */
	public static void createComputer(ComputerDTO computerDTO) {
		client.target(URI_COMPUTER).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(computerDTO, MediaType.APPLICATION_JSON));
	}

	/**
	 * Permet de mettre à jour un computer.
	 * @param computerDTO les nouvelles infos
	 * @return le nouveau computer
	 */
	public static ComputerDTO updateComputer(ComputerDTO computerDTO) {
		System.out.println(computerDTO);
		client.target(URI_COMPUTER).path(String.valueOf(computerDTO.getId())).request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(computerDTO, MediaType.APPLICATION_JSON));
		return computerDTO;
	}

	/**
	 * Permet de récupérer une page de la liste des computers.
	 * @param page la page à afficher
	 * @param resultPerPage le nombre de computers par page
	 * @return la liste des computers à afficher
	 */
	public static List<ComputerDTO> getComputers(int page, int resultPerPage) {
		return client.target(URI_COMPUTER + "?page=" + page + "&resultPerPage=" + resultPerPage)
				.request(MediaType.APPLICATION_JSON).get(new GenericType<List<ComputerDTO>>() {
				});
	}

	/**
	 * Permet de récupérer une page de la liste des companies.
	 * @param page la page à afficher
	 * @param resultPerPage le nombre de companies par page
	 * @return la liste des companies à afficher
	 */
	public static List<Company> getCompanies(int page, int resultPerPage) {
		return client.target(URI_COMPANY + "?page=" + page + "&resultPerPage=" + resultPerPage)
				.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Company>>() {
				});
	}

	/**
	 * Permet de récupérer le nombre de companies.
	 * @return le nombre de companies
	 */
	public static int getCountCompanies() {
		return client.target(URI_COMPANY).path("/count").request(MediaType.APPLICATION_JSON).get(Integer.class);
	}

	/**
	 * Permet de récupérer le nombre de computers.
	 * @return le nombre de computers
	 */
	public static int getCountComputers() {
		return client.target(URI_COMPUTER).path("/count").request(MediaType.APPLICATION_JSON).get(Integer.class);
	}

	/**
	 * Permet de supprimer un computer.
	 * @param id l'id du computerà supprimer
	 */
	public static void deleteComputer(String id) {
		client.target(URI_COMPUTER).path(id).request(MediaType.APPLICATION_JSON).delete();
	}

	/**
	 * Permet de supprimer une company.
	 * @param id l'id de la company à afficher
	 */
	public static void deleteCompany(String id) {
		client.target(URI_COMPANY).path(id).request(MediaType.APPLICATION_JSON).delete();
	}

}
