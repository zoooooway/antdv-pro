package org.zooway.antdvpro.convert;

import org.mapstruct.Mapper;
import org.zooway.antdvpro.model.dto.AuthMenuDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionNode;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthPermissionPO;
import org.zooway.antdvpro.model.po.AuthRolePO;
import org.zooway.antdvpro.model.po.AuthUserPO;
import org.zooway.antdvpro.model.request.AuthRoleDMO;
import org.zooway.antdvpro.model.request.AuthUserDMO;

/**
 * @author zooway
 * @create 2024/1/9
 */
@Mapper(componentModel = "spring")
public interface AuthEntityConvert {
    AuthRoleDTO toAuthRoleDTO(AuthRolePO authRolePO);

    AuthRolePO toAuthRolePO(AuthRoleDTO authRoleDTO);

    AuthRolePO toAuthRolePO(AuthRoleDMO authRoleDMO);

    AuthMenuDTO toAuthMenuDTO(AuthPermissionPO authPermissionPO);

    AuthUserPO toAuthUserPO(AuthUserDMO authUser);

    AuthPermissionNode toAuthPermissionNode(AuthPermissionPO rootPermission);
}
