package com.excilys.cdb.enums;

/**
 * Enum pour la gestion des choix dans le menu des computers.
 */
public enum ComputerChoice {

    SELECTPAGE("1", "1 - Select the page to show"), DETAILSCOMPUTER("2",
            "2 - Show the details of one computer"), CREATECOMPUTER("3", "3 - Create a computer"), UPDATECOMPUTER("4",
                    "4 - Update a computer"), DELETECOMPUTER("5",
                            "5 - Delete a computer"), BACKMENU("6", "6 - Back to the menu");

    private String value = "";
    private String text = "";

    /**
     * Constructeur avec parametre.
     * @param value
     *            la valeur de l'enum
     * @param text
     *            le texte de l'enum
     */
    ComputerChoice(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * Permet de récupérer l'enum à partir d'un string.
     * @param s
     *            le string à trouver
     * @return l'enum correspondant
     */
    public static ComputerChoice get(String s) {
        for (ComputerChoice computerChoice : ComputerChoice.values()) {
            if (s.equalsIgnoreCase(computerChoice.value)) {
                return computerChoice;
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return text;
    }
}