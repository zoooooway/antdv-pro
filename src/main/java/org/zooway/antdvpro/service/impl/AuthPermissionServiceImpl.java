package org.zooway.antdvpro.service.impl;

import io.mybatis.mapper.example.Example;
import io.mybatis.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.auth.enums.ActionEnum;
import org.zooway.antdvpro.convert.AuthEntityConvert;
import org.zooway.antdvpro.exception.BadRequestException;
import org.zooway.antdvpro.exception.ResourceNotFoundException;
import org.zooway.antdvpro.mapper.AuthPermissionMapper;
import org.zooway.antdvpro.mapper.AuthRolePermissionMapper;
import org.zooway.antdvpro.model.dto.ActionEntityDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionDTO;
import org.zooway.antdvpro.model.dto.AuthPermissionNode;
import org.zooway.antdvpro.model.po.AuthPermissionPO;
import org.zooway.antdvpro.model.po.AuthRolePermissionPO;
import org.zooway.antdvpro.service.AuthPermissionService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.zooway.antdvpro.auth.enums.PermissionEnum.*;

/**
 * auth_permission - 菜单
 *
 * @author zooway
 */
@Slf4j
@Service
public class AuthPermissionServiceImpl extends AbstractService<AuthPermissionPO, Long, AuthPermissionMapper> implements AuthPermissionService {
    public static final List<ActionEntityDTO> ALL_ACTION = Arrays.stream(ActionEnum.values()).map(e -> new ActionEntityDTO(e.key, e.label)).collect(toList());

    @Autowired
    private AuthPermissionMapper authPermissionMapper;
    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Autowired
    private AuthEntityConvert authEntityConvert;

    @Override
    public List<AuthPermissionDTO> getAuthorityPermissions() {
        List<AuthPermissionPO> permissionPOList = authPermissionMapper.selectList(null);

        return permissionPOList.stream()
                // 只有模块可以配置权限
                .filter(p -> Objects.equals(MODULE.getType(), p.getType())).map(p -> {
                    AuthPermissionDTO authPermission = new AuthPermissionDTO();
                    authPermission.setActionList(ALL_ACTION);
                    authPermission.setPermissionId(p.getId());
                    authPermission.setPermissionKey(p.getKey());
                    authPermission.setPermissionName(p.getTitle());

                    return authPermission;
                }).collect(Collectors.toList());

    }

    @Override
    public List<AuthPermissionNode> getPermissionTree() {
        List<AuthPermissionPO> permissionPOList = authPermissionMapper.selectList(null);
        Map<Long, List<AuthPermissionPO>> group = permissionPOList.stream().collect(Collectors.groupingBy(AuthPermissionPO::getParentId));

        Optional<AuthPermissionPO> any = permissionPOList.stream().filter(p -> ROOT_PERMISSION_ID.equals(p.getId())).findAny();
        if (!any.isPresent()) {
            throw new RuntimeException("未找到根节点");
        }
        AuthPermissionPO rootPermission = any.get();

        AuthPermissionNode rootNode = authEntityConvert.toAuthPermissionNode(rootPermission);

        recursionToBuild(rootNode, group);

        return Collections.singletonList(rootNode);
    }

    @Override
    public Integer addPermission(AuthPermissionPO authPermission, LoginUser loginUser) {
        authPermission.setCreateUser(loginUser.getUsername());
        authPermission.setUpdateUser(loginUser.getUsername());

        validate(authPermission.getType(), authPermission.getParentId());

        int i = authPermissionMapper.insertSelective(authPermission);
        return i;
    }

    private void validate(Integer type, Long parentId) {
        Optional<AuthPermissionPO> ppOp = authPermissionMapper.selectByPrimaryKey(parentId);
        if (!ppOp.isPresent()) {
            throw new BadRequestException("不存在的父菜单!");
        }

        AuthPermissionPO parent = ppOp.get();
        // 为减少复杂度, 我们规定:
        // 只有type为模块的菜单可以配置权限, 且不允许模块下再次声明模块
        // 目录的父菜单只能是目录
        // 模块的父菜单只能是目录
        // 页面的父菜单只能是模块
        if (CATALOGUE.getType().equals(type) && !CATALOGUE.getType().equals(parent.getType())) {
            // 不允许模块下再次声明模块
            throw new BadRequestException("目录的父菜单只能是目录!");
        }
        if (MODULE.getType().equals(type) && !CATALOGUE.getType().equals(parent.getType())) {
            // 不允许模块下再次声明模块
            throw new BadRequestException("模块的父菜单只能是目录!");
        }
        if (PAGE.getType().equals(type) && !MODULE.getType().equals(parent.getType())) {
            // 不允许模块下再次声明模块
            throw new BadRequestException("页面的父菜单只能是模块!");
        }
    }

    @Override
    public Integer editPermission(AuthPermissionPO authPermission, LoginUser loginUser) {
        authPermission.setUpdateUser(loginUser.getUsername());

        Optional<AuthPermissionPO> pOp = authPermissionMapper.selectByPrimaryKey(authPermission.getId());
        if (!pOp.isPresent()) {
            throw new ResourceNotFoundException("未找到此资源");
        }
        AuthPermissionPO permissionPO = pOp.get();

        if (authPermission.getType() != null) {
            Long parentId = authPermission.getParentId() == null ? permissionPO.getParentId() : authPermission.getParentId();
            validate(authPermission.getType(), parentId);
        }

        int i = authPermissionMapper.updateByPrimaryKeySelective(authPermission);
        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deletePermission(Long id, LoginUser loginUser) {
        List<AuthPermissionPO> allPermissions = authPermissionMapper.selectList(null);
        // 找到所有子节点
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        allPermissions.forEach(p -> {
            if (ids.contains(p.getParentId())) {
                ids.add(p.getParentId());
            }
        });
        log.info("删除菜单: {}", ids);
        // 删除所有节点
        Example<AuthPermissionPO> example = new Example<>();
        example.createCriteria().andIn(AuthPermissionPO::getId, ids);
        int i = authPermissionMapper.deleteByExample(example);

        // 删除与角色的关联关系
        Example<AuthRolePermissionPO> erp = new Example<>();
        erp.createCriteria().andIn(AuthRolePermissionPO::getPermissionId, ids);

        authRolePermissionMapper.deleteByExample(erp);

        return i;
    }

    /**
     * 递归的构建节点树
     *
     * @param node
     * @param group
     */
    private void recursionToBuild(AuthPermissionNode node, Map<Long, List<AuthPermissionPO>> group) {
        Long id = node.getId();

        List<AuthPermissionPO> children = group.get(id);
        if (!CollectionUtils.isEmpty(children)) {
            List<AuthPermissionNode> collect = children.stream().map(p -> {
                AuthPermissionNode childNode = authEntityConvert.toAuthPermissionNode(p);
                recursionToBuild(childNode, group);
                return childNode;
            }).collect(Collectors.toList());
            node.setChildren(collect);
        }
    }


}
