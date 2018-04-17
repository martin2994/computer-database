package main.java.com.excilys.cdb.enums;

public enum MenuChoice { 
	LISTCOMPUTER("1","1 - Show the computer list"),
	LISTCOMPANY("2","2 - Show the company list"),
	QUIT("q","q - Quit");
	
	private String value = "";
	private String text = "";
	
	MenuChoice(String value,String text) {
		this.value = value;
		this.text = text;
	}
	
	public static MenuChoice get(String s) {
		for (MenuChoice menuChoice : MenuChoice.values()) {
			if(s.equalsIgnoreCase(menuChoice.value)) {
				return menuChoice;
			}
		}
		return null;
	}
	
	public String toString() {
		return text;
	}
}
