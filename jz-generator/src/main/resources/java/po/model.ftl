package ${config.basePackage}.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
<#if model.hasDate>
  import java.util.Date;
</#if>
<#if model.hasBigDecimal>
import java.math.BigDecimal;
</#if>
import lombok.Data;

/**
* 表[${model.table}]对应实体类
*
* @author ${config.author}
*
*/

@Data
@TableName(value = "`${model.table}`", schema="${model.database}")
@ApiModel(value = "表${model.table}的实体类")
public class ${model.model} {
  <#list model.fieldList as field>

    /**
    * ${field.comment}
    */
    @ApiModelProperty(value = "${field.comment}", dataType = "${field.type}")
    @TableField("`${field.field}`")
    <#if (field.primaryKey && field.autoIncrement)>
    @TableId(value="`${field.field}`",type = IdType.AUTO)
    </#if>
    private ${field.type} ${field.camelCaseField};
  </#list>

}
