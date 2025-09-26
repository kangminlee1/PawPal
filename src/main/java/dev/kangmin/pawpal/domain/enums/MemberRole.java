package dev.kangmin.pawpal.domain.enums;

public enum MemberRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;
    MemberRole(String value){
        this.value = value;
    }
}
