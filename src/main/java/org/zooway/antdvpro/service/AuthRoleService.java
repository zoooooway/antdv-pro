package org.zooway.antdvpro.service;

import io.mybatis.service.BaseService;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthRolePO;
import org.zooway.antdvpro.model.request.AuthRoleDMO;

import java.util.List;

/**
 * auth_role - 角色表
 *
 * @author zooway
 */
public interface AuthRoleService extends BaseService<AuthRolePO, Long> {

    List<AuthRoleDTO> getRoleList(List<Long> roleIds);

    Integer addRole(AuthRoleDMO authRole, LoginUser loginUser);

    Integer editRole(AuthRoleDMO authRole, LoginUser loginUser);

    Integer deleteRole(Long roleId, LoginUser loginUser);
}
