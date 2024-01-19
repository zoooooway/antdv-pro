package ${package};

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mybatis.provider.Entity;
import org.apache.ibatis.type.JdbcType;

import org.zooway.antdvpro.common.validation.ValidInterfaces;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

<#list it.importJavaTypes as javaType>
import ${javaType};
</#list>

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@Data
@Entity.Table(value = "${it.name}", remark = "${it.comment}", autoResultMap = true)
public class ${it.name.className}PO implements Serializable {

    private static final long serialVersionUID = 1L;

    <#list it.columns as column>
    <#if column.pk>
    @Entity.Column(value = "${column.name}", id = true, remark = "${column.comment}", updatable = false, insertable = false, useGeneratedKeys = true)
    @NotNull(groups = ValidInterfaces.Edit.class)
    @Min(value = 1L, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    @Max(value = Long.MAX_VALUE, groups = {ValidInterfaces.Edit.class, ValidInterfaces.Insert.class})
    <#elseif column.javaType == 'Date'>
    @Entity.Column(value = "${column.name}", remark = "${column.comment}"<#if column.tags.jdbcType>, jdbcType = JdbcType.${column.jdbcType}</#if>)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#else>
    @Entity.Column(value = "${column.name}", remark = "${column.comment}"<#if column.tags.jdbcType>, jdbcType = JdbcType.${column.jdbcType}</#if>)
    </#if>
    private ${column.javaType} ${column.name.fieldName};

    </#list>
}
