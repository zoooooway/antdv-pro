package org.zooway.antdvpro.auth.enums;

/**
 * @author zooway
 * @create 2024/1/16
 */
public enum ActionEnum {
    ADD("add", "新增"),
    DELETE("delete", "删除"),
    EDIT("edit", "修改"),
    QUERY("query", "查询"),
    GET("get", "详情"),
    ENABLE("enable", "启用"),
    DISABLE("disable", "禁用"),
    IMPORT("import", "导入"),
    EXPORT("export", "导出");


    public final String key;
    public final String label;

    ActionEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

}
