package org.zooway.antdvpro.mapper;

import org.zooway.antdvpro.model.po.AuthUserRolePO;
import io.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * auth_user_role - 用户角色关联
 *
 * @author zooway
 */
@Mapper
public interface AuthUserRoleMapper extends BaseMapper<AuthUserRolePO, Long> {

}