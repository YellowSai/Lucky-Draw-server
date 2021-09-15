package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.SysCity;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

/**
* 表[sys_city]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class SysCityUpdateDTO {

    /**
    * id,非自增
    */
    @Min(value = 1, message = "id不能为空")
    @ApiModelProperty(value = "id,非自增", dataType = "long")
    private long id;

    /**
    * 父级id
    */
    @Min(value = 0, message = "父级id不能为空")
    @ApiModelProperty(value = "父级id", dataType = "long")
    private long parentId;

    /**
    * 分类名
    */
    @ApiModelProperty(value = "分类名", dataType = "String")
    private String name;

    /**
    * 拼音
    */
    @ApiModelProperty(value = "拼音", dataType = "String")
    private String spell;

    /**
    * 排序权重
    */
    @ApiModelProperty(value = "排序权重", dataType = "int")
    private int weight;

    /**
    * 级别
    */
    @ApiModelProperty(value = "级别", dataType = "int")
    private int grade;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    private int dataStatus;
    public SysCity toSysCity() {
        SysCity sysCity = new SysCity();
        BeanUtil.copyProperties(this, sysCity);
        return sysCity;
    }
}
