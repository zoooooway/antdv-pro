package org.zooway.antdvpro.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zooway.antdvpro.model.dto.ActionEntityDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author zooway
 * @create 2024/1/9
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthPermissionActionDMO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long permissionId;
    /**
     * 用户实际被授予的权限操作
     */
    private List<ActionEntityDTO> actionList;
}
