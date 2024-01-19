package org.zooway.antdvpro.mapper;

import org.zooway.antdvpro.model.po.AuthPermissionPO;
import io.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * auth_permission - 菜单
 *
 * @author zooway
 */
@Mapper
public interface AuthPermissionMapper extends BaseMapper<AuthPermissionPO, Long> {

}