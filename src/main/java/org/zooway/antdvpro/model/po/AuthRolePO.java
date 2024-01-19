package org.zooway.antdvpro.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mybatis.provider.Entity;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.zooway.antdvpro.common.validation.ValidInterfaces;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * auth_role - 角色表
 *
 * @author zooway
 */
@Data
@Entity.Table(value = "auth_role", remark = "角色表", autoResultMap = true)
public class AuthRolePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Entity.Column(value = "id", id = true, remark = "主键", updatable = false, insertable = false, useGeneratedKeys = true)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long id;

    @Entity.Column(value = "role_key", remark = "")
    private String roleKey;

    @Entity.Column(value = "role_name", remark = "")
    private String roleName;

    @Entity.Column(value = "description", remark = "")
    private String description;

    @Entity.Column(value = "create_user", remark = "")
    private String createUser;

    @Entity.Column(value = "create_time", remark = "", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Entity.Column(value = "update_user", remark = "")
    private String updateUser;

    @Entity.Column(value = "update_time", remark = "", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
