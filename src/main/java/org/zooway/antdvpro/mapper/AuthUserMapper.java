package org.zooway.antdvpro.mapper;

import io.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zooway.antdvpro.model.dto.AuthUserDTO;
import org.zooway.antdvpro.model.po.AuthUserPO;

import java.util.List;

/**
 * auth_user - 用户表
 *
 * @author zooway
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUserPO, Long> {

    List<AuthUserDTO> selectUserList(@Param("username") String username, @Param("roles") String roles);
}