package com.excilys.cdb.enums;

public enum ExceptionMessage {
    BAD_ACCESS("text.error.bad_access"), NO_RESULT("text.error.no_result"), NO_COMPUTER_TO_UPDATE(
            "text.error.no_computer_update"), NO_COMPUTER_TO_CREATE("text.error.no_computer_create"), INVALID_ID(
                    "text.error.invalid_id"), NO_COMPANY("text.error.no_company"), UNCOMPLETE_INFO(
                            "text.error.uncomplete_info"), NO_COMPUTER("text.error.no_computer"), INVALID_INFO(
                                    "text.error.invalid_info"), ERROR("text.error");

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
