package org.zooway.antdvpro.auth.provider;

import io.mybatis.mapper.example.Example;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.mapper.AuthUserMapper;
import org.zooway.antdvpro.mapper.AuthUserRoleMapper;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthUserPO;
import org.zooway.antdvpro.model.po.AuthUserRolePO;
import org.zooway.antdvpro.service.AuthRoleService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * 用户验证处理
 *
 * @author zooway
 * @create 2024/1/2
 */
@Slf4j
@Lazy
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;

    @Autowired
    private AuthRoleService authRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Example<AuthUserPO> ep = new Example<>();
        ep.createCriteria()
                .andEqualTo(AuthUserPO::getUsername, username);

        Optional<AuthUserPO> userOp = authUserMapper.selectOneByExample(ep);
        if (!userOp.isPresent()) {
            log.warn("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }

        return createLoginUser(userOp.get());
    }

    public UserDetails createLoginUser(AuthUserPO userPO) {
        LoginUser loginUser = new LoginUser();
        loginUser.setAuthUser(userPO);

        Example<AuthUserRolePO> eur = new Example<>();
        eur.createCriteria()
                .andEqualTo(AuthUserRolePO::getUserId, userPO.getId());
        List<AuthUserRolePO> userRolePOS = authUserRoleMapper.selectByExample(eur);

        List<Long> roleIds = userRolePOS.stream().map(AuthUserRolePO::getRoleId).collect(toList());
        List<AuthRoleDTO> authRoleDTOList = authRoleService.getRoleList(roleIds);

        loginUser.setRoles(authRoleDTOList);
        return loginUser;
    }
}
