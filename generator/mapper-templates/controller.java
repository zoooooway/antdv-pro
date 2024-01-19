package ${package};

import ${project.attrs.basePackage}.model.base.Resp;

import ${project.attrs.basePackage}.model.po.${it.name.className}PO;
import ${project.attrs.basePackage}.service.${it.name.className}Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@RestController
@RequestMapping("${it.name.fieldName}")
public class ${it.name.className}Controller {

    @Autowired
    private ${it.name.className}Service ${it.name.fieldName}Service;

    @PostMapping
    public Resp save(@RequestBody ${it.name.className}PO ${it.name.fieldName}) {
        return Resp.ok(${it.name.fieldName}Service.saveSelective(${it.name.fieldName}));
    }

    @GetMapping
    public Resp findList(${it.name.className}PO ${it.name.fieldName}) {
        return Resp.ok(${it.name.fieldName}Service.findList(${it.name.fieldName}));
    }

    @GetMapping(value = "/{id}")
    public Resp findById(@PathVariable("id") Long id) {
        return Resp.ok(${it.name.fieldName}Service.findById(id));
    }

    @PutMapping(value = "/{id}")
    public Resp update(@PathVariable("id") Long id, @RequestBody ${it.name.className}PO ${it.name.fieldName}) {
        ${it.name.fieldName}.setId(id);
        return Resp.ok(${it.name.fieldName}Service.updateSelective(${it.name.fieldName}));
    }

    @DeleteMapping(value = "/{id}")
    public Resp deleteById(@PathVariable("id") Long id) {
        return Resp.ok(${it.name.fieldName}Service.deleteById(id) == 1);
    }

}
