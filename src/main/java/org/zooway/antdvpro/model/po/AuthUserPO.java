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
 * auth_user - 用户表
 *
 * @author zooway
 */
@Data
@Entity.Table(value = "auth_user", remark = "用户表", autoResultMap = true)
public class AuthUserPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Entity.Column(value = "id", id = true, remark = "主键", updatable = false, insertable = false, useGeneratedKeys = true)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long id;

    @Entity.Column(value = "username", remark = "用户名")
    private String username;

    @Entity.Column(value = "password", remark = "密码")
    private String password;

    @Entity.Column(value = "create_time", remark = "创建日期", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Entity.Column(value = "create_user", remark = "创建人")
    private String createUser;

    @Entity.Column(value = "update_time", remark = "更新日期", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Entity.Column(value = "update_user", remark = "更新人")
    private String updateUser;

    @Entity.Column(value = "last_login", remark = "最后登录", jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;

}
