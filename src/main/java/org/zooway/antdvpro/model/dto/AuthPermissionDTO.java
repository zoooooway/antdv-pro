package org.zooway.antdvpro.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zooway
 * @create 2024/1/9
 */
@Data
public class AuthPermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long permissionId;
    private String permissionKey;
    private String permissionName;


    /**
     * 用户实际被授予的权限操作
     */
    private List<ActionEntityDTO> actionList;
    private String dataAccess;
}
