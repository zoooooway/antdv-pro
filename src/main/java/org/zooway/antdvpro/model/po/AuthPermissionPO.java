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
 * auth_permission - 菜单
 *
 * @author zooway
 */
@Data
@Entity.Table(value = "auth_permission", remark = "菜单", autoResultMap = true)
public class AuthPermissionPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Entity.Column(value = "id", id = true, remark = "主键", updatable = false, insertable = false, useGeneratedKeys = true)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    private Long id;

    @Entity.Column(value = "`key`", remark = "")
    private String key;

    @Entity.Column(value = "title", remark = "")
    private String title;

    @Entity.Column(value = "icon", remark = "")
    private String icon;

    @Entity.Column(value = "path", remark = "路由路径, 为空会使用key与父路由的key进行拼接")
    private String path;

    @Entity.Column(value = "component", remark = "")
    private String component;

    @Entity.Column(value = "redirect", remark = "重定向地址, 为空表示不重写重定向地址")
    private String redirect;

    @Entity.Column(value = "target", remark = "")
    private String target;

    @Entity.Column(value = "`show`", remark = "")
    private Boolean show;

    @Entity.Column(value = "hide_children", remark = "是否隐藏子菜单")
    private Boolean hideChildren;

    @Entity.Column(value = "hidden_header_content", remark = "是否隐藏隐藏面包屑和页面标题栏")
    private Boolean hiddenHeaderContent;

    @Entity.Column(value = "parent_id", remark = "父路由id")
    private Long parentId;

    @Entity.Column(value = "type", remark = "此菜单类型, 可选值: 0: 目录, 1: 模块, 2: 页面. ")
    private Integer type;

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
