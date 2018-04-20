package com.excilys.cdb.utils;

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
	
	private int currentPage;
	
	private int maxPage;

	/**
	 * constructeur qui initialise la liste
	 */
	public Page() {
		results = new ArrayList<>();
	}
	
	public Page(int currentPage,int maxPage) {
		this.currentPage = currentPage;
		this.maxPage = maxPage;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
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
