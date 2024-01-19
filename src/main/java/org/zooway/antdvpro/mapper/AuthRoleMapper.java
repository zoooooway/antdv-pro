package org.zooway.antdvpro.mapper;

import org.zooway.antdvpro.model.po.AuthRolePO;
import io.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * auth_role - 角色表
 *
 * @author zooway
 */
@Mapper
public interface AuthRoleMapper extends BaseMapper<AuthRolePO, Long> {

}