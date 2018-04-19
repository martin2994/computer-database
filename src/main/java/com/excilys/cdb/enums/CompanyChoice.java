package main.java.com.excilys.cdb.enums;

/**
 * Enum pour la gestion des choix dans le menu des company
 *
 */
public enum CompanyChoice {
	SELECTPAGE("1","1 - Select the page to show"),
	BACK("2","2 - Back to the menu");
	
	private String value = "";
	private String text = "";
	
	CompanyChoice(String value,String text) {
		this.value = value;
		this.text = text;
	}
	
	public static CompanyChoice get(String s) {
		for (CompanyChoice companyChoice : CompanyChoice.values()) {
			if(s.equalsIgnoreCase(companyChoice.value)) {
				return companyChoice;
			}
		}
		return null;
	}
	
	public String toString() {
		return text;
	}
}
