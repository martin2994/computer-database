package main.java.com.excilys.cdb.enums;

public enum ComputerChoice {

	SELECTPAGE("1","1 - Select the page to show"),
	DETAILSCOMPUTER("2","2 - Show the details of one computer"),
	CREATECOMPUTER("3","3 - Create a computer"),
	UPDATECOMPUTER("4","4 - Update a computer"),
	DELETECOMPUTER("5","5 - Delete a computer"),
	BACKMENU("6","6 - Back to the menu");
	
	private String value = "";
	private String text = "";
	
	ComputerChoice(String value,String text) {
		this.value = value;
		this.text = text;
	}
	
	public static ComputerChoice get(String s) {
		for (ComputerChoice computerChoice : ComputerChoice.values()) {
			if(s.equalsIgnoreCase(computerChoice.value)) {
				return computerChoice;
			}
		}
		return null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString() {
		return text;
	}
}