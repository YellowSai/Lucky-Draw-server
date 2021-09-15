package cn.marsLottery.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 表[sys_city]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_city`")
@ApiModel(value = "表sys_city的实体类")
public class SysCity {

    /**
    * id,非自增
    */
    @ApiModelProperty(value = "id,非自增", dataType = "long")
    @TableField("`id`")
    private long id;

    /**
    * 父级id
    */
    @ApiModelProperty(value = "父级id", dataType = "long")
    @TableField("`parent_id`")
    private long parentId;

    /**
    * 分类名
    */
    @ApiModelProperty(value = "分类名", dataType = "String")
    @TableField("`name`")
    private String name;

    /**
    * 拼音
    */
    @ApiModelProperty(value = "拼音", dataType = "String")
    @TableField("`spell`")
    private String spell;

    /**
    * 排序权重
    */
    @ApiModelProperty(value = "排序权重", dataType = "int")
    @TableField("`weight`")
    private int weight;

    /**
    * 级别
    */
    @ApiModelProperty(value = "级别", dataType = "int")
    @TableField("`grade`")
    private int grade;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    @TableField("`data_status`")
    private int dataStatus;

}
