package org.zooway.antdvpro.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zooway
 * @create 2024/1/11
 */
@Data
public class AuthUserDMO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private Long roleId;
}