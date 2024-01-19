package org.zooway.antdvpro.auth.entity;

import lombok.Data;

/**
 * @author zooway
 * @create 2024/1/2
 */
@Data
public class LoginRequest {
    private String userName;
    private String password;

}
