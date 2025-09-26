package dev.kangmin.pawpal.domain.enums;

public enum ExistStatus {
    EXISTS("EXISTS"),
    DELETED("DELETED");

    private String value;

    ExistStatus(String value) {
        this.value = value;
    }

}
