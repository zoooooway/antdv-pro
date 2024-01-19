package org.zooway.antdvpro.service.impl;

import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;
import org.zooway.antdvpro.mapper.AuthRolePermissionMapper;
import org.zooway.antdvpro.model.po.AuthRolePermissionPO;
import org.zooway.antdvpro.service.AuthRolePermissionService;

/**
 * auth_role_permission - 角色权限关联
 *
 * @author zooway
 */
@Service
public class AuthRolePermissionServiceImpl extends AbstractService<AuthRolePermissionPO, Long, AuthRolePermissionMapper> implements AuthRolePermissionService {

}
