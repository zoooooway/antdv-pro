package org.zooway.antdvpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zooway.antdvpro.auth.annotation.CurrentUser;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.base.Resp;
import org.zooway.antdvpro.model.po.AuthPermissionPO;
import org.zooway.antdvpro.service.AuthPermissionService;

/**
 * auth_permission - 菜单
 *
 * @author zooway
 */
@RestController
@RequestMapping("/api/v1/permission")
public class AuthPermissionController {

    @Autowired
    private AuthPermissionService authPermissionService;

    @PreAuthorize("@pg.hasPermission('permission:add')")
    @PostMapping
    public Resp save(@RequestBody AuthPermissionPO authPermission, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authPermissionService.addPermission(authPermission, loginUser));
    }

    @PreAuthorize("@pg.hasPermission('permission:query')")
    @GetMapping(value = "/authority")
    public Resp getAuthorityPermissions() {
        return Resp.ok(authPermissionService.getAuthorityPermissions());
    }

    @PreAuthorize("@pg.hasPermission('permission:query')")
    @GetMapping(value = "/tree")
    public Resp getPermissionTree() {
        return Resp.ok(authPermissionService.getPermissionTree());
    }

    @PreAuthorize("@pg.hasPermission('permission:get')")
    @GetMapping(value = "/{id}")
    public Resp findById(@PathVariable("id") Long id) {
        return Resp.ok(authPermissionService.findById(id));
    }

    @PreAuthorize("@pg.hasPermission('permission:edit')")
    @PutMapping(value = "/{id}")
    public Resp update(@PathVariable("id") Long id, @RequestBody AuthPermissionPO authPermission, @CurrentUser LoginUser loginUser) {
        authPermission.setId(id);
        return Resp.ok(authPermissionService.editPermission(authPermission, loginUser));
    }

    @PreAuthorize("@pg.hasPermission('permission:delete')")
    @DeleteMapping(value = "/{id}")
    public Resp deleteById(@PathVariable("id") Long id, @CurrentUser LoginUser loginUser) {
        return Resp.ok(authPermissionService.deletePermission(id, loginUser));
    }

}
