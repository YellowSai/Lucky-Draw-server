package cn.marsLottery.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
  import java.util.Date;
import lombok.Data;

/**
* 表[sys_log]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_log`")
@ApiModel(value = "表sys_log的实体类")
public class SysLog {

    /**
    * id,非自增
    */
    @ApiModelProperty(value = "id,非自增", dataType = "int")
    @TableField("`id`")
    private int id;

    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id", dataType = "int")
    @TableField("`user_id`")
    private int userId;

    /**
    * 用户名
    */
    @ApiModelProperty(value = "用户名", dataType = "String")
    @TableField("`user_name`")
    private String userName;

    /**
    * 模块
    */
    @ApiModelProperty(value = "模块", dataType = "String")
    @TableField("`module`")
    private String module;

    /**
    * 功能描述
    */
    @ApiModelProperty(value = "功能描述", dataType = "String")
    @TableField("`description`")
    private String description;

    /**
    * 操作内容
    */
    @ApiModelProperty(value = "操作内容", dataType = "String")
    @TableField("`content`")
    private String content;

    /**
    * ip
    */
    @ApiModelProperty(value = "ip", dataType = "String")
    @TableField("`ip`")
    private String ip;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField("`create_time`")
    private Date createTime;

}
