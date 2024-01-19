package org.zooway.antdvpro.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zooway
 * @create 2024/1/12
 */
@Data
public class AuthUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String roleName;
    private Long roleId;
    private Date createTime;
    private Date lastLogin;
}
