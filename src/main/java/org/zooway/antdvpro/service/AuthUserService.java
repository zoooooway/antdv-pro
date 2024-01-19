package org.zooway.antdvpro.service;

import io.mybatis.service.BaseService;
import org.springframework.transaction.annotation.Transactional;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.base.PageResp;
import org.zooway.antdvpro.model.dto.AuthUserDTO;
import org.zooway.antdvpro.model.po.AuthUserPO;
import org.zooway.antdvpro.model.request.AuthUserDMO;

/**
 * auth_user - 用户表
 *
 * @author zooway
 */
public interface AuthUserService extends BaseService<AuthUserPO, Long> {

    PageResp<AuthUserDTO> getUserList(String username, String roles, Integer pageNum, Integer pageSize);

    @Transactional(rollbackFor = Exception.class)
    Integer addUser(AuthUserDMO authUser, LoginUser loginUser);

    @Transactional(rollbackFor = Exception.class)
    Integer editUser(AuthUserDMO authUser, LoginUser loginUser);

    @Transactional(rollbackFor = Exception.class)
    Integer deleteUser(Long userId, LoginUser loginUser);
}
