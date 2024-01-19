package org.zooway.antdvpro.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import io.mybatis.mapper.example.Example;
import io.mybatis.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.auth.enums.RoleEnum;
import org.zooway.antdvpro.convert.AuthEntityConvert;
import org.zooway.antdvpro.exception.BadRequestException;
import org.zooway.antdvpro.exception.ResourceNotFoundException;
import org.zooway.antdvpro.exception.SaveResourceFailedException;
import org.zooway.antdvpro.mapper.AuthPermissionMapper;
import org.zooway.antdvpro.mapper.AuthRoleMapper;
import org.zooway.antdvpro.mapper.AuthRolePermissionMapper;
import org.zooway.antdvpro.mapper.AuthUserRoleMapper;
import org.zooway.antdvpro.model.dto.ActionEntityDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionDTO;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthPermissionPO;
import org.zooway.antdvpro.model.po.AuthRolePO;
import org.zooway.antdvpro.model.po.AuthRolePermissionPO;
import org.zooway.antdvpro.model.po.AuthUserRolePO;
import org.zooway.antdvpro.model.request.AuthPermissionActionDMO;
import org.zooway.antdvpro.model.request.AuthRoleDMO;
import org.zooway.antdvpro.service.AuthRoleService;
import org.zooway.antdvpro.util.JsonUtil;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.zooway.antdvpro.auth.enums.PermissionEnum.MODULE;
import static org.zooway.antdvpro.service.impl.AuthPermissionServiceImpl.ALL_ACTION;

/**
 * auth_role - 角色表
 *
 * @author zooway
 */
@Service
public class AuthRoleServiceImpl extends AbstractService<AuthRolePO, Long, AuthRoleMapper> implements AuthRoleService {

    @Autowired
    private AuthRoleMapper authRoleMapper;
    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Autowired
    private AuthPermissionMapper authPermissionMapper;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;

    @Autowired
    private AuthEntityConvert authEntityConvert;

    @Override
    public List<AuthRoleDTO> getRoleList(List<Long> roleIds) {

        List<AuthRolePO> roleList;
        if (CollectionUtils.isEmpty(roleIds)) {
            roleList = authRoleMapper.selectList(null);

        } else {
            Example<AuthRolePO> epRole = new Example<>();
            epRole.createCriteria()
                    .andIn(AuthRolePO::getId, roleIds);
            roleList = authRoleMapper.selectByExample(epRole);
        }

        // 获取角色列表
        List<AuthRoleDTO> authRoleDTOList = roleList.stream().map(r -> authEntityConvert.toAuthRoleDTO(r)).collect(toList());
        Map<Long, AuthRolePO> roleId2Role = roleList.stream().collect(toMap(AuthRolePO::getId, r -> r));

        Example<AuthUserRolePO> eaur = new Example<>();
        eaur.createCriteria()
                .andIn(AuthUserRolePO::getRoleId, roleId2Role.keySet());
        Map<Long, Long> r2UserCount = authUserRoleMapper.selectByExample(eaur).stream().collect(groupingBy(AuthUserRolePO::getRoleId, counting()));

        // 获取所有权限
        List<AuthPermissionPO> allPermissionList = authPermissionMapper.selectList(null);
        Map<Long, AuthPermissionPO> perm2Perm = allPermissionList.stream().collect(toMap(AuthPermissionPO::getId, p -> p));

        // 获取角色与权限的关联关系
        Example<AuthRolePermissionPO> erp = new Example<>();
        erp.createCriteria()
                .andIn(AuthRolePermissionPO::getRoleId, roleId2Role.keySet());
        List<AuthRolePermissionPO> rolePermissionPOList = authRolePermissionMapper.selectByExample(erp);
        Map<Long, List<AuthRolePermissionPO>> roleId2Permission = rolePermissionPOList.stream().collect(groupingBy(AuthRolePermissionPO::getRoleId));

        // 超级管理员并不在表中包含关联关系, 但其应该拥有所有权限
        Optional<AuthRolePO> rootOp = roleList.stream().filter(r -> RoleEnum.SUPPER.getKey().equals(r.getRoleKey())).findAny();
        if (rootOp.isPresent()) {
            // 构造管理员与所有权限的关联关系
            AuthRolePO root = rootOp.get();

            List<AuthRolePermissionPO> allRolePermission = allPermissionList.stream().map(p -> {
                AuthRolePermissionPO arp = new AuthRolePermissionPO();
                arp.setRoleId(root.getId());
                arp.setPermissionId(p.getId());

                arp.setActions(JsonUtil.writeJson(ALL_ACTION));
                return arp;
            }).collect(toList());

            roleId2Permission.put(root.getId(), allRolePermission);
        }

        authRoleDTOList.forEach(ar -> {
            Long roleId = ar.getId();

            List<AuthRolePermissionPO> rolePermissionPOS = roleId2Permission.getOrDefault(roleId, Collections.emptyList());
            List<AuthPermissionDTO> authPermissionDTOList = rolePermissionPOS.stream().map(rp -> {
                AuthPermissionDTO authPermission = new AuthPermissionDTO();

                Long permissionId = rp.getPermissionId();
                AuthPermissionPO permissionPO = perm2Perm.get(permissionId);

                // 只有模块才可配置权限
                if (!Objects.equals(MODULE.getType(), permissionPO.getType())) {
                    return null;
                }


                String actions = rp.getActions();
                // 角色被授予的权限
                List<ActionEntityDTO> list = JsonUtil.readJson(actions, new TypeReference<List<ActionEntityDTO>>() {
                });
                authPermission.setActionList(ALL_ACTION.stream().filter(list::contains)
                        // 不可以改动流中的原始数据
                        .map(e -> new ActionEntityDTO(e.getAction(), e.getDescribe()))
                        .collect(toList()));

                authPermission.setPermissionId(permissionPO.getId());
                authPermission.setPermissionKey(permissionPO.getKey());
                authPermission.setPermissionName(permissionPO.getTitle());

                return authPermission;
            }).filter(Objects::nonNull).collect(toList());

            ar.setUserCount(Math.toIntExact(r2UserCount.getOrDefault(roleId, 0L)));
            ar.setPermissions(authPermissionDTOList);
        });
        return authRoleDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addRole(AuthRoleDMO authRole, LoginUser loginUser) {
        AuthRolePO authRolePO = authEntityConvert.toAuthRolePO(authRole);
        authRolePO.setCreateUser(loginUser.getUsername());
        authRolePO.setUpdateUser(loginUser.getUsername());

        int i = authRoleMapper.insertSelective(authRolePO);

        final Long roleId = authRolePO.getId();
        if (roleId == null) {
            throw new SaveResourceFailedException("保存角色信息时异常");
        }

        List<AuthRolePermissionPO> authRolePermissionPOS = extractRolePermission(authRole, roleId);

        authRolePermissionMapper.insertList(authRolePermissionPOS);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer editRole(AuthRoleDMO authRole, LoginUser loginUser) {
        final Long roleId = authRole.getId();
        Optional<AuthRolePO> rOp = authRoleMapper.selectByPrimaryKey(roleId);
        if (!rOp.isPresent()) {
            throw new ResourceNotFoundException("尝试修改不存在的角色!");
        }

        AuthRolePO role = rOp.get();
        if (RoleEnum.SUPPER.getKey().equals(role.getRoleKey())) {
            throw new BadRequestException("不允许修改超级管理员!");
        }

        AuthRolePO authRolePO = authEntityConvert.toAuthRolePO(authRole);
        authRolePO.setUpdateUser(loginUser.getUsername());

        int i = authRoleMapper.updateByPrimaryKeySelective(authRolePO);

        if (roleId == null) {
            throw new SaveResourceFailedException("保存角色信息时异常");
        }

        // 先清除原先的关联关系
        Example<AuthRolePermissionPO> earp = new Example<>();
        earp.createCriteria()
                .andEqualTo(AuthRolePermissionPO::getRoleId, roleId);
        authRolePermissionMapper.deleteByExample(earp);

        List<AuthRolePermissionPO> authRolePermissionPOS = extractRolePermission(authRole, roleId);

        authRolePermissionMapper.insertList(authRolePermissionPOS);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteRole(Long roleId, LoginUser loginUser) {
        // 不允许删除超级管理员
        Optional<AuthRolePO> rOp = authRoleMapper.selectByPrimaryKey(roleId);
        if (!rOp.isPresent()) {
            throw new ResourceNotFoundException("尝试删除不存在的角色!");
        }

        AuthRolePO role = rOp.get();
        if (RoleEnum.SUPPER.getKey().equals(role.getRoleKey())) {
            throw new BadRequestException("不允许删除超级管理员!");
        }

        // 查找该角色下是否关联用户
        Example<AuthUserRolePO> eur = new Example<>();
        eur.createCriteria()
                .andEqualTo(AuthUserRolePO::getRoleId, roleId);
        List<AuthUserRolePO> authUserRolePOS = authUserRoleMapper.selectByExample(eur);
        if (!CollectionUtils.isEmpty(authUserRolePOS)) {
            throw new BadRequestException("该角色关联了多个用户, 若要删除, 请先解除关联关系!");
        }

        // 删除此角色
        int i = authRoleMapper.deleteByPrimaryKey(roleId);

        // 删除此角色关联的权限
        Example<AuthRolePermissionPO> earp = new Example<>();
        earp.createCriteria()
                .andEqualTo(AuthRolePermissionPO::getRoleId, roleId);
        authRolePermissionMapper.deleteByExample(earp);

        return i;
    }

    private List<AuthRolePermissionPO> extractRolePermission(AuthRoleDMO authRole, Long roleId) {
        List<AuthPermissionPO> allPermissions = authPermissionMapper.selectList(null);
        Map<Long, AuthPermissionPO> pId2P = allPermissions.stream().collect(toMap(AuthPermissionPO::getId, p -> p));
        Map<Long, List<AuthPermissionPO>> groupByParentId = allPermissions.stream().collect(groupingBy(AuthPermissionPO::getParentId));

        // 保存与权限的关联关系
        // 为减少复杂度, 我们规定, 只有type为模块的菜单可以配置权限, 且不允许模块下再次声明模块
        // 所以, 其余类型的菜单的权限都应为[]
        // 因此处理权限应当遵守以下约定:
        // 当选择一个子权限时, 应当向上回溯所有的父权限 , 等同于获取所有必要的父权限
        // 同时, 向下获取该节点下的所有子权限
        Set<AuthPermissionActionDMO> permissionSet = new HashSet<>();
        authRole.getPermissions().forEach(p -> {
            // 递归的获取必要的父节点
            backtrackingParentPermission(p, permissionSet, pId2P);
            // 递归的获取其下的子节点
            trackingSubPermission(p, permissionSet, groupByParentId);
        });


        return permissionSet.stream().map(p -> {
            AuthPermissionPO permission = pId2P.get(p.getPermissionId());
            if (permission == null) {
                return null;
            }

            AuthRolePermissionPO arp = new AuthRolePermissionPO();
            arp.setRoleId(roleId);
            arp.setPermissionId(permission.getId());
            List<ActionEntityDTO> actionList = p.getActionList();
            if (CollectionUtils.isEmpty(actionList)) {
                actionList = Collections.emptyList();
            }
            arp.setActions(JsonUtil.writeJson(actionList));
            return arp;
        }).filter(Objects::nonNull).collect(toList());
    }

    private void backtrackingParentPermission(AuthPermissionActionDMO permission, Set<AuthPermissionActionDMO> permissionSet, Map<Long, AuthPermissionPO> pId2P) {
        permissionSet.add(permission);

        // 向上找寻父节点
        Long permissionId = permission.getPermissionId();
        AuthPermissionPO permissionPO = pId2P.get(permissionId);
        if (permissionPO == null) {
            return;
        }

        Long parentId = permissionPO.getParentId();
        AuthPermissionPO parentP = pId2P.get(parentId);
        if (parentP == null) {
            return;
        }

        // 由于规定只有模块可以配置权限, 因此父子节点只需要为空即可
        backtrackingParentPermission(new AuthPermissionActionDMO(parentId, Collections.emptyList()), permissionSet, pId2P);
    }

    private void trackingSubPermission(AuthPermissionActionDMO permission, Set<AuthPermissionActionDMO> permissionSet, Map<Long, List<AuthPermissionPO>> groupByParentId) {
        permissionSet.add(permission);

        // 向下寻找所有子节点
        Long permissionId = permission.getPermissionId();
        List<AuthPermissionPO> children = groupByParentId.get(permissionId);
        if (children == null) {
            return;
        }

        children.forEach(p -> {
            // 由于规定只有模块可以配置权限, 不允许模块下存在模块, 因此子节点只需要为空即可
            trackingSubPermission(new AuthPermissionActionDMO(p.getId(), Collections.emptyList()), permissionSet, groupByParentId);
        });


    }
}
