package org.zooway.antdvpro.model.po;

import io.mybatis.provider.Entity;
import lombok.Data;
import org.zooway.antdvpro.common.validation.ValidInterfaces;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * auth_role_permission - 角色权限关联
 *
 * @author zooway
 */
@Data
@Entity.Table(value = "auth_role_permission", remark = "角色权限关联", autoResultMap = true)
public class AuthRolePermissionPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Entity.Column(value = "role_id", id = true, remark = "角色id", updatable = false)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long roleId;

    @Entity.Column(value = "permission_id", id = true, remark = "菜单id", updatable = false)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long permissionId;

    @Entity.Column(value = "actions", remark = "权限指令集")
    private String actions;

}
