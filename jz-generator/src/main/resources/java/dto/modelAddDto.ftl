package ${config.basePackage}.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import ${config.basePackage}.po.${model.model};

<#if model.hasDate>
    import java.util.Date;
</#if>
<#if model.hasBigDecimal>
    import java.math.BigDecimal;
</#if>

/**
* 表[${model.table}]对应AddDTO类
*
* @author ${config.author}
*
*/

@Data
public class ${model.model}AddDTO {
<#list model.fieldList as field>

    /**
    * ${field.comment}
    */
    @ApiModelProperty(value = "${field.comment}", dataType = "${field.type}")
    private ${field.type} ${field.camelCaseField};
</#list>
    public ${model.model} to${model.model}() {
        ${model.model} ${model.camelCaseField} = new ${model.model}();
        BeanUtil.copyProperties(this, ${model.camelCaseField});
        return ${model.camelCaseField};
    }
}
