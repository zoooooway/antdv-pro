package org.zooway.antdvpro.auth.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zooway.antdvpro.auth.entity.LoginUser;

import javax.annotation.Nullable;

/**
 * @author zooway
 * @create 2024/1/6
 */
public class SecurityHelper {


    /**
     * 获取当前登录的用户
     *
     * @return 如果没有登录, 将返回null
     */
    @Nullable
    public static LoginUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser currentUser = authentication == null ? null : (LoginUser) authentication.getPrincipal();
        return currentUser;
    }
}
