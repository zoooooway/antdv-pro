package org.zooway.antdvpro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zooway.antdvpro.auth.annotation.CurrentUser;
import org.zooway.antdvpro.auth.entity.LoginRequest;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.base.Resp;
import org.zooway.antdvpro.model.dto.AuthMenuDTO;
import org.zooway.antdvpro.service.AuthService;

import java.util.List;


/**
 * @author zooway
 * @create 2024/1/2
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Resp login(@RequestBody LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        // 生成令牌
        String token = authService.login(userName, password);
        return Resp.ok(token);
    }

    @GetMapping("/info")
    public Resp userInfo(@CurrentUser LoginUser loginUser) {
        return Resp.ok(loginUser);
    }

    @GetMapping("/nav")
    public Resp menus(@CurrentUser LoginUser loginUser) {
        List<AuthMenuDTO> menuList = authService.getUserMenu(loginUser);
        return Resp.ok(menuList);
    }

    @PostMapping("/logout")
    public Resp logout(@CurrentUser LoginUser loginUser) {
        if (loginUser != null) {
            authService.logout(loginUser);
        }
        return Resp.ok();
    }
}
