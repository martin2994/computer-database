package main.java.com.excilys.cdb.enums;

public enum PageChoice {
	SELECTPAGE("1","1 - Select the page to show"),
	BACK("2","2 - Back to the menu");
	
	private String value = "";
	private String text = "";
	
	PageChoice(String value,String text) {
		this.value = value;
		this.text = text;
	}
	
	public static PageChoice get(String s) {
		for (PageChoice pageChoice : PageChoice.values()) {
			if(s.equalsIgnoreCase(pageChoice.value)) {
				return pageChoice;
			}
		}
		return null;
	}
	
	public String toString() {
		return text;
	}
}
