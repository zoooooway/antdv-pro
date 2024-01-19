package org.zooway.antdvpro.mapper;

import org.zooway.antdvpro.model.po.AuthRolePermissionPO;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.list.ListMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * auth_role_permission - 角色权限关联
 *
 * @author zooway
 */
@Mapper
public interface AuthRolePermissionMapper extends BaseMapper<AuthRolePermissionPO, Long>, ListMapper<AuthRolePermissionPO> {

}