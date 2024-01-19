package org.zooway.antdvpro.service;

import io.mybatis.service.BaseService;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.dto.AuthPermissionDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionNode;
import org.zooway.antdvpro.model.po.AuthPermissionPO;

import java.util.List;

/**
 * auth_permission - 菜单
 *
 * @author zooway
 */
public interface AuthPermissionService extends BaseService<AuthPermissionPO, Long> {
    Long ROOT_PERMISSION_ID = 1L;

    /**
     * 获取可授权的菜单
     *
     * @return
     */
    List<AuthPermissionDTO> getAuthorityPermissions();

    List<AuthPermissionNode> getPermissionTree();

    Integer addPermission(AuthPermissionPO authPermission, LoginUser loginUser);

    Integer editPermission(AuthPermissionPO authPermission, LoginUser loginUser);

    Integer deletePermission(Long id, LoginUser loginUser);

}
