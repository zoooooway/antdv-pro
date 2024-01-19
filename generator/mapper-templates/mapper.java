package ${package};

import io.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import ${project.attrs.basePackage}.model.po.${it.name.className}PO;
import ${project.attrs.basePackage}.service.${it.name.className}Service;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@Mapper
public interface ${it.name.className}Mapper extends BaseMapper<${it.name.className}PO, Long> {

}