package ${config.basePackage}.vo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ${config.basePackage}.po.${model.model};

<#if model.hasDate>
    import java.util.Date;
</#if>
<#if model.hasBigDecimal>
    import java.math.BigDecimal;
</#if>
import lombok.Data;

/**
* 表[${model.table}]对应VO类
*
* @author ${config.author}
*
*/

@Data
public class ${model.model}VO {
<#list model.fieldList as field>

    /**
    * ${field.comment}
    */
    @ApiModelProperty(value = "${field.comment}", dataType = "${field.type}")
    private ${field.type} ${field.camelCaseField};
</#list>

    public ${model.model}VO(${model.model} ${model.camelCaseField}) {
        if (${model.camelCaseField} != null) {
            BeanUtil.copyProperties(${model.camelCaseField}, this);
        }
    }
}
