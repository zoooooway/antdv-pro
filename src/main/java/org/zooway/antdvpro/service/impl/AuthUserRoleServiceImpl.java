package org.zooway.antdvpro.service.impl;

import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;
import org.zooway.antdvpro.mapper.AuthUserRoleMapper;
import org.zooway.antdvpro.model.po.AuthUserRolePO;
import org.zooway.antdvpro.service.AuthUserRoleService;

/**
 * auth_user_role - 用户角色关联
 *
 * @author zooway
 */
@Service
public class AuthUserRoleServiceImpl extends AbstractService<AuthUserRolePO, Long, AuthUserRoleMapper> implements AuthUserRoleService {

}
