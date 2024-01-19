package org.zooway.antdvpro.auth.enums;

/**
 * @author zooway
 * @create 2024/1/2
 */
public enum RoleEnum {
    SUPPER("SUPPER"),
    OFFICIAL("STAFF");

    private final String key;

    RoleEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
