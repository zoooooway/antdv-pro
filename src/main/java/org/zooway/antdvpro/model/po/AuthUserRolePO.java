package org.zooway.antdvpro.model.po;

import io.mybatis.provider.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zooway.antdvpro.common.validation.ValidInterfaces;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * auth_user_role - 用户角色关联
 *
 * @author zooway
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity.Table(value = "auth_user_role", remark = "用户角色关联", autoResultMap = true)
public class AuthUserRolePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Entity.Column(value = "user_id", id = true, remark = "用户id", updatable = false)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long userId;

    @Entity.Column(value = "role_id", id = true, remark = "角色id", updatable = false)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long roleId;

}
