package org.zooway.antdvpro.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.mybatis.mapper.example.Example;
import io.mybatis.service.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.convert.AuthEntityConvert;
import org.zooway.antdvpro.exception.BadRequestException;
import org.zooway.antdvpro.exception.OperationRejectedException;
import org.zooway.antdvpro.exception.ResourceNotFoundException;
import org.zooway.antdvpro.exception.SaveResourceFailedException;
import org.zooway.antdvpro.mapper.AuthUserMapper;
import org.zooway.antdvpro.mapper.AuthUserRoleMapper;
import org.zooway.antdvpro.model.base.PageResp;
import org.zooway.antdvpro.model.dto.AuthUserDTO;
import org.zooway.antdvpro.model.po.AuthUserPO;
import org.zooway.antdvpro.model.po.AuthUserRolePO;
import org.zooway.antdvpro.model.request.AuthUserDMO;
import org.zooway.antdvpro.service.AuthUserService;

import java.util.Optional;

/**
 * auth_user - 用户表
 *
 * @author zooway
 */
@Service
public class AuthUserServiceImpl extends AbstractService<AuthUserPO, Long, AuthUserMapper> implements AuthUserService {

    public static final Long ROOT_USER_ID = 1L;

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;
    @Autowired
    private AuthEntityConvert authEntityConvert;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public PageResp<AuthUserDTO> getUserList(String username, String roles, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<AuthUserDTO> page = (Page<AuthUserDTO>) authUserMapper.selectUserList(username, roles);

        return new PageResp<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addUser(AuthUserDMO authUser, LoginUser loginUser) {
        // 密码进行散列加密
        authUser.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));

        AuthUserPO authUserPO = authEntityConvert.toAuthUserPO(authUser);
        authUserPO.setCreateUser(loginUser.getUsername());
        authUserPO.setUpdateUser(loginUser.getUsername());

        int i = authUserMapper.insertSelective(authUserPO);

        final Long userId = authUserPO.getId();
        if (userId == null) {
            throw new SaveResourceFailedException("保存用户信息时异常");
        }

        Long roleId = authUser.getRoleId();
        AuthUserRolePO authUserRolePO = new AuthUserRolePO(userId, roleId);
        authUserRoleMapper.insertSelective(authUserRolePO);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer editUser(AuthUserDMO authUser, LoginUser loginUser) {
        final Long userId = authUser.getId();
        Optional<AuthUserPO> rOp = authUserMapper.selectByPrimaryKey(userId);
        if (!rOp.isPresent()) {
            throw new ResourceNotFoundException("尝试修改不存在的角色!");
        }

        AuthUserPO user = rOp.get();
        if (ROOT_USER_ID.equals(user.getId())) {
            if (!loginUser.checkRoot() || authUser.getRoleId() != null) {
                throw new OperationRejectedException("不允许修改超级管理员!");
            }
        }

        if (StringUtils.isNotBlank(authUser.getPassword())) {
            // 密码进行散列加密
            authUser.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));
        }

        AuthUserPO authUserPO = authEntityConvert.toAuthUserPO(authUser);
        authUserPO.setUpdateUser(loginUser.getUsername());

        int i = authUserMapper.updateByPrimaryKeySelective(authUserPO);

        if (userId == null) {
            throw new SaveResourceFailedException("保存角色信息时异常");
        }

        if (authUser.getRoleId() != null) {
            // 先清除原先的关联关系
            Example<AuthUserRolePO> eaur = new Example<>();
            eaur.createCriteria().andEqualTo(AuthUserRolePO::getUserId, userId);
            authUserRoleMapper.deleteByExample(eaur);

            Long roleId = authUser.getRoleId();
            AuthUserRolePO authUserRolePO = new AuthUserRolePO(userId, roleId);
            authUserRoleMapper.insertSelective(authUserRolePO);
        }

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteUser(Long userId, LoginUser loginUser) {
        Optional<AuthUserPO> rOp = authUserMapper.selectByPrimaryKey(userId);
        if (!rOp.isPresent()) {
            throw new ResourceNotFoundException("尝试删除不存在的角色!");
        }

        AuthUserPO user = rOp.get();
        if (ROOT_USER_ID.equals(user.getId())) {
            throw new BadRequestException("不允许删除超级管理员!");

        }

        // 删除此用户
        return authUserMapper.deleteByPrimaryKey(userId);
    }
}
