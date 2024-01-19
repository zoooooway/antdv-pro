package org.zooway.antdvpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zooway.antdvpro.auth.annotation.CurrentUser;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.base.Resp;
import org.zooway.antdvpro.model.po.AuthRolePO;
import org.zooway.antdvpro.model.request.AuthRoleDMO;
import org.zooway.antdvpro.service.AuthRoleService;

import java.util.Collections;

/**
 * auth_role - 角色表
 *
 * @author zooway
 */
@RestController
@RequestMapping("/api/v1/role")
public class AuthRoleController {

    @Autowired
    private AuthRoleService authRoleService;

    @PreAuthorize("@pg.hasPermission('role:add')")
    @PostMapping
    public Resp save(@RequestBody AuthRoleDMO authRole, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authRoleService.addRole(authRole, loginUser));
    }

    @PreAuthorize("@pg.hasPermission('role:query')")
    @GetMapping
    public Resp selectList() {
        return Resp.ok(authRoleService.getRoleList(Collections.emptyList()));
    }

    @PreAuthorize("@pg.hasPermission('role:query')")
    @GetMapping("/selections")
    public Resp findList(AuthRolePO authRolePO) {
        return Resp.ok(authRoleService.findList(authRolePO));
    }

    @PreAuthorize("@pg.hasPermission('role:get')")
    @GetMapping(value = "/{id}")
    public Resp findById(@PathVariable("id") Long id) {
        return Resp.ok(authRoleService.findById(id));
    }

    @PreAuthorize("@pg.hasPermission('role:edit')")
    @PutMapping(value = "/{id}")
    public Resp update(@PathVariable("id") Long id, @RequestBody AuthRoleDMO authRole, @CurrentUser LoginUser loginUser) {
        authRole.setId(id);
        return Resp.ok(authRoleService.editRole(authRole, loginUser));
    }

    @PreAuthorize("@pg.hasPermission('role:delete')")
    @DeleteMapping(value = "/{id}")
    public Resp deleteById(@PathVariable("id") Long id, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authRoleService.deleteRole(id, loginUser));
    }

}
