package org.zooway.antdvpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zooway.antdvpro.auth.annotation.CurrentUser;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.base.Resp;
import org.zooway.antdvpro.model.request.AuthUserDMO;
import org.zooway.antdvpro.service.AuthUserService;

/**
 * auth_user - 用户表
 *
 * @author zooway
 */
@RestController
@RequestMapping("/api/v1/user")
public class AuthUserController {

    @Autowired
    private AuthUserService authUserService;

    @PreAuthorize("@pg.hasPermission('user:add')")
    @PostMapping
    public Resp save(@RequestBody AuthUserDMO authUser, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authUserService.addUser(authUser, loginUser));
    }

    /**
     * @param username 用户名, 模糊查询
     * @param roles    以逗号分隔的权限标识
     * @return
     */
    @PreAuthorize("@pg.hasPermission('user:query')")
    @GetMapping
    public Resp findList(@RequestParam(name = "username", required = false) String username,
                         @RequestParam(name = "roles", required = false) String roles,
                         @RequestParam(name = "pageNum") Integer pageNum,
                         @RequestParam(name = "pageSize") Integer pageSize
    ) {
        return Resp.ok(authUserService.getUserList(username, roles, pageNum, pageSize));
    }

    @PreAuthorize("@pg.hasPermission('user:get')")
    @GetMapping(value = "/{id}")
    public Resp findById(@PathVariable("id") Long id) {
        return Resp.ok(authUserService.findById(id));
    }

    @PreAuthorize("@pg.hasPermission('user:edit')")
    @PutMapping(value = "/{id}")
    public Resp update(@PathVariable("id") Long id, @RequestBody AuthUserDMO authUser, @CurrentUser LoginUser loginUser) {
        authUser.setId(id);
        return Resp.ok(authUserService.editUser(authUser, loginUser));
    }

    @PreAuthorize("@pg.hasPermission('user:delete')")
    @DeleteMapping(value = "/{id}")
    public Resp deleteById(@PathVariable("id") Long id, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authUserService.deleteUser(id, loginUser));
    }

}
