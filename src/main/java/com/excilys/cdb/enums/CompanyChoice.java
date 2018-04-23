package com.excilys.cdb.enums;

/**
 * Enum pour la gestion des choix dans le menu des company.
 *
 */
public enum CompanyChoice {
    SELECTPAGE("1", "1 - Select the page to show"), BACK("2", "2 - Back to the menu");

    private String value = "";
    private String text = "";

    /**
     * Constructeur avec parametre.
     * @param value la valeur de l'enum
     * @param text le texte de l'enum
     */
    CompanyChoice(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * Permet de récupérer l'enum à partir d'un string.
     * @param s le string à trouver
     * @return l'enum correspondant
     */
    public static CompanyChoice get(String s) {
        for (CompanyChoice companyChoice : CompanyChoice.values()) {
            if (s.equalsIgnoreCase(companyChoice.value)) {
                return companyChoice;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
