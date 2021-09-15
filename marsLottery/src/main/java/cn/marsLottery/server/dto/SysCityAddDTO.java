package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.SysCity;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


/**
* 表[sys_city]对应AddDTO类
*
* @author auto
*
*/

@Data
public class SysCityAddDTO {

    @Min(value = 0, message = "父级id不能为空")
    @ApiModelProperty(value = "父级id", dataType = "long")
    private long parentId;

    @NotEmpty(message = "分类名不能为空")
    @ApiModelProperty(value = "分类名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "排序权重", dataType = "int")
    private int weight;

    public SysCity toSysCity() {
        SysCity sysCity = new SysCity();
        BeanUtil.copyProperties(this, sysCity);
        return sysCity;
    }
}
