package dev.kangmin.pawpal.domain.enums;

public enum VaccineType {
    DHPPL("DHPPL"),
    CORONA("CORONA"),
    RABIES("RABIES");

    private String value;

    VaccineType(String value) {
        this.value = value;
    }

}
