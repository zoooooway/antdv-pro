package org.zooway.antdvpro.auth.provider;

import org.springframework.stereotype.Service;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.dto.ActionEntityDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionDTO;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author zooway
 * @create 2024/1/16
 */
@Service(value = "pg")
public class PermissionGuard {


    public boolean hasPermission(@Nonnull String permissionStr) {
        LoginUser loginUser = SecurityHelper.currentUser();
        if (loginUser == null) {
            return false;
        }

        if (loginUser.checkRoot()) {
            return true;
        }

        List<AuthRoleDTO> roles = loginUser.getRoles();
        for (AuthRoleDTO role : roles) {
            List<AuthPermissionDTO> permissions = role.getPermissions();
            for (AuthPermissionDTO permission : permissions) {
                for (ActionEntityDTO action : permission.getActionList()) {
                    if (permissionStr.equals(permission.getPermissionKey() + ":" + action.getAction())) {
                        return true;
                    }

                }
            }
        }

        return false;
    }
}
