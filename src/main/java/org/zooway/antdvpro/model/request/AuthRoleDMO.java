package org.zooway.antdvpro.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于操纵authorole的实体类
 *
 * @author zooway
 * @create 2024/1/11
 */
@Data
public class AuthRoleDMO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleKey;
    private String roleName;
    private String description;
    private List<AuthPermissionActionDMO> permissions;
}