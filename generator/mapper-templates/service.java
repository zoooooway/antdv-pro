package ${package};

import io.mybatis.service.BaseService;

import ${project.attrs.basePackage}.model.po.${it.name.className}PO;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
public interface ${it.name.className}Service extends BaseService<${it.name.className}PO, Long> {

}
