package main.java.com.excilys.cdb.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Regroupe des objets sous forme de Page
 * liste de n éléments
 * 
 * @param <T> l'objet stocké
 */
public class Page<T> {

	/**
	 * La liste d'objet stocké
	 */
	private List<T> results;
	
	/**
	 * le nombre d'objet par page
	 */
	public final static int resultsPerPage = 5;

	/**
	 * constructeur qui initialise la liste
	 */
	public Page() {
		results = new ArrayList<>();
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	/**
	 * Ajoute un objet dans la liste d'objet
	 * @param t l'objet à ajouter
	 */
	public void add(T t) {
		if (!results.contains(t) && t != null) {
			results.add(t);
		}
	}

}
