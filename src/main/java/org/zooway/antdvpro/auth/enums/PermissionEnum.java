package org.zooway.antdvpro.auth.enums;

/**
 * @author zooway
 * @create 2024/1/2
 */
public enum PermissionEnum {
    CATALOGUE(0),
    MODULE(1),
    PAGE(2);

    private final Integer type;

    PermissionEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
