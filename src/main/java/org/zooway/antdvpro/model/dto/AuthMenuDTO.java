package org.zooway.antdvpro.model.dto;

import lombok.Data;

/**
 * @author zooway
 * @create 2024/1/9
 */
@Data
public class AuthMenuDTO {
    private Long id;
    private String key;
    private String path;
    private Long parentId;
    private String redirect;
    private String component;
    private MetaDTO meta;

    @Data
    public static class MetaDTO {
        private String title;
        private Boolean hiddenHeaderContent;
        private Boolean hideChildren;
        private Boolean show;
    }
}
