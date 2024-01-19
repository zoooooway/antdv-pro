package org.zooway.antdvpro.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zooway
 * @create 2024/1/9
 */
@Data
public class AuthRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleKey;
    private String roleName;
    private String description;
    private Integer userCount;
    private List<AuthPermissionDTO> permissions;
}
