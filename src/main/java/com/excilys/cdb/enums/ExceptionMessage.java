package com.excilys.cdb.enums;

public enum ExceptionMessage {
    BAD_ACCESS("Bad access"), NO_RESULT(" No result"), NO_COMPUTER_TO_UPDATE(
            "No computer to update"), NO_COMPUTER_TO_CREATE("No computer to add"), INVALID_ID(
                    "Invalid id"), NO_COMPANY("This company does not exist"), UNCOMPLETE_INFO(
                            "Uncomplete informations"), NO_COMPUTER("This computer does not exist"), INVALID_INFO(
                                    "Invalid informations"), ERROR("An error occured");

    private String message;

    /**
     * Constructeur.
     * @param message
     *            le message de l'enum
     */
    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
