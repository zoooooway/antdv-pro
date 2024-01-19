package org.zooway.antdvpro.service.impl;

import io.mybatis.mapper.example.Example;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.auth.provider.TokenService;
import org.zooway.antdvpro.convert.AuthEntityConvert;
import org.zooway.antdvpro.exception.BusinessException;
import org.zooway.antdvpro.mapper.AuthPermissionMapper;
import org.zooway.antdvpro.mapper.AuthRolePermissionMapper;
import org.zooway.antdvpro.mapper.AuthUserMapper;
import org.zooway.antdvpro.model.dto.AuthMenuDTO;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthPermissionPO;
import org.zooway.antdvpro.model.po.AuthRolePermissionPO;
import org.zooway.antdvpro.model.po.AuthUserPO;
import org.zooway.antdvpro.service.AuthService;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 登录校验方法
 *
 * @author zooway
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthEntityConvert authEntityConvert;

    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthPermissionMapper authPermissionMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public String login(String username, String password) {

        // 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            log.error("用户名密码不匹配!用户名：{}", username);
            throw new BadCredentialsException("认证信息异常!");
        } catch (Exception e) {
            log.error("登录失败!用户名" + username, e);
            throw new BusinessException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
        log.info("登录成功!用户名：{}", username);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        String token = tokenService.createToken(loginUser);
        // 异步的修改用户的上次登录时间
        Date date = new Date();
        Long id = loginUser.getAuthUser().getId();
        threadPoolTaskExecutor.execute(() -> {
            AuthUserPO u = new AuthUserPO();
            u.setId(id);
            u.setLastLogin(date);
            authUserMapper.updateByPrimaryKeySelective(u);
        });
        return token;
    }

    @Override
    public void logout(LoginUser loginUser) {
        String uuid = loginUser.getUuid();
        tokenService.delLoginUser(uuid);
    }

    @Override
    public List<AuthMenuDTO> getUserMenu(LoginUser loginUser) {
        List<AuthRoleDTO> roles = loginUser.getRoles();

        List<AuthPermissionPO> permissionPOList;
        boolean isRoot = loginUser.checkRoot();
        if (isRoot) {
            permissionPOList = authPermissionMapper.selectList(null);
        } else {
            List<Long> roleIds = roles.stream().map(AuthRoleDTO::getId).collect(toList());
            Example<AuthRolePermissionPO> erp = new Example<>();
            erp.createCriteria()
                    .andIn(AuthRolePermissionPO::getRoleId, roleIds);
            List<AuthRolePermissionPO> rolePermissionPOList = authRolePermissionMapper.selectByExample(erp);
            List<Long> permissionIds = rolePermissionPOList.stream().map(AuthRolePermissionPO::getPermissionId).collect(toList());

            Example<AuthPermissionPO> epPermission = new Example<>();
            epPermission.createCriteria()
                    .andIn(AuthPermissionPO::getId, permissionIds);
            permissionPOList = authPermissionMapper.selectByExample(epPermission);
        }

        List<AuthMenuDTO> authMenuDTOList = permissionPOList.stream().map(p -> {
            AuthMenuDTO authMenuDTO = authEntityConvert.toAuthMenuDTO(p);
            AuthMenuDTO.MetaDTO meta = new AuthMenuDTO.MetaDTO();
            meta.setShow(p.getShow());
            meta.setTitle(p.getTitle());
            meta.setHideChildren(p.getHideChildren());
            meta.setHiddenHeaderContent(p.getHideChildren());

            authMenuDTO.setMeta(meta);
            return authMenuDTO;
        }).collect(toList());

        return authMenuDTOList;
    }
}
