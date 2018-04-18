package main.java.com.excilys.cdb.enums;

public enum PageChoice {
	PREVIOUS("1","1 - PREVIOUS"),
	NEXT("2","2 - NEXT"),
	BACK("3","3 - Back to the menu");
	
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
