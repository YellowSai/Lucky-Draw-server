package ${config.basePackage}.controller.admapi;

import ${config.basePackage}.dto.${model.model}AddDTO;
import ${config.basePackage}.dto.${model.model}UpdateDTO;
import ${config.basePackage}.po.${model.model};
import ${config.basePackage}.service.${model.model}Service;
import ${config.basePackage}.vo.${model.model}VO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import cn.jzcscw.commons.core.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 表[${model.table}]对应的后台管理类
*
* @author ${config.author}
*
*/

@Slf4j
@RestController
@RequestMapping("/admapi/${model.camelCaseField}")
@Api(tags = {"${model.comment}管理接口"})
public class ${model.model}AdminController {

    @Autowired
    private ${model.model}Service ${model.camelCaseField}Service;

    @ApiOperation(value = "${model.comment}-列表")
    @GetMapping("/list")
    public Result<Page<${model.model}>> list(Page page) {
        LambdaQueryWrapper<${model.model}> queryWrapper = new LambdaQueryWrapper<>();
        //FIXME 根据需要增加搜索条件
        Page<${model.model}> pageData = ${model.camelCaseField}Service.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "${model.comment}-详情")
    @GetMapping("/detail")
    public Result<${model.model}VO> detail(int id) {
        if (id <= 0) {
            return Result.error("${model.comment}不存在");
        }
        ${model.model} ${model.camelCaseField} = ${model.camelCaseField}Service.getById(id);
        if (${model.camelCaseField} == null) {
            return Result.error("${model.comment}不存在");
        }
        ${model.model}VO ${model.camelCaseField}VO = new ${model.model}VO(${model.camelCaseField});
        return Result.data(${model.camelCaseField}VO);
    }

    @ApiOperation(value = "${model.comment}-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated ${model.model}AddDTO ${model.camelCaseField}AddDTO) {
        ${model.model} ${model.camelCaseField} = ${model.camelCaseField}AddDTO.to${model.model}();
        ${model.camelCaseField}Service.save(${model.camelCaseField});
        return Result.ok("添加${model.comment}成功");
    }

    @ApiOperation(value = "${model.comment}-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated ${model.model}UpdateDTO ${model.camelCaseField}UpdateDTO) {
        ${model.model} old${model.model} = ${model.camelCaseField}Service.getById(${model.camelCaseField}UpdateDTO.getId());
        if (old${model.model} == null) {
            return Result.error("${model.comment}不存在");
        }
        ${model.model} ${model.camelCaseField} = ${model.camelCaseField}UpdateDTO.to${model.model}();
        BeanUtils.copyProperties(${model.camelCaseField}, old${model.model});
        ${model.camelCaseField}Service.updateById(${model.camelCaseField});
        return Result.ok("编辑${model.comment}成功");
    }

    @ApiOperation(value = "${model.comment}-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("${model.comment}不存在");
        }
        ${model.model} ${model.camelCaseField} = ${model.camelCaseField}Service.getById(id);
        if (${model.camelCaseField} == null) {
            return Result.error("${model.comment}不存在");
        }
        ${model.camelCaseField}Service.removeById(id);
        return Result.ok("删除${model.comment}成功");
    }
}
