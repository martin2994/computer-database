package main.java.com.excilys.cdb.enums;

public enum ComputerChoice {
	
	DETAILSCOMPUTER("1","1 - Show the details of one computer"),
	CREATECOMPUTER("2","2 - Create a computer"),
	UPDATECOMPUTER("3","3 - Update a computer"),
	DELETECOMPUTER("4","4 - Delete a computer"),
	SHOWLIST("5", "5 - Show the computer list"),
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