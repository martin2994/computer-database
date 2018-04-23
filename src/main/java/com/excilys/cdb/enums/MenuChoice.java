package com.excilys.cdb.enums;

/**
 * Enum pour la gestion des choix dans le menu principal.
 *
 */
public enum MenuChoice {
    LISTCOMPUTER("1", "1 - Show the computer list"), LISTCOMPANY("2", "2 - Show the company list"), QUIT("q",
            "q - Quit");

    private String value = "";
    private String text = "";

    /**
     * Constructeur avec parametre.
     * @param value
     *            la valeur de l'enum
     * @param text
     *            le texte de l'enum
     */
    MenuChoice(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * Permet de récupérer l'enum à partir d'un string.
     * @param s
     *            le string à trouver
     * @return l'enum correspondant
     */
    public static MenuChoice get(String s) {
        for (MenuChoice menuChoice : MenuChoice.values()) {
            if (s.equalsIgnoreCase(menuChoice.value)) {
                return menuChoice;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
