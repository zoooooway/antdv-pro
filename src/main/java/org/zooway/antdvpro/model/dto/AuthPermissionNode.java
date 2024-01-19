package org.zooway.antdvpro.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zooway
 * @create 2024/1/15
 */
@Data
public class AuthPermissionNode {

    private Long id;

    private String key;

    private String title;

    private String icon;

    private String path;

    private String component;

    private String redirect;

    private String target;

    private Boolean show;

    private Boolean hideChildren;

    private Boolean hiddenHeaderContent;

    private Long parentId;

    private Integer type;

    private List<ActionEntityDTO> actionEntitySet;

    private List<AuthPermissionNode> children;
}
