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
* 表[sys_attachment]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_attachment`", schema="mars_lottery")
@ApiModel(value = "表sys_attachment的实体类")
public class SysAttachment {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 存储路径
    */
    @ApiModelProperty(value = "存储路径", dataType = "String")
    @TableField("`path`")
    private String path;

    /**
    * url路径
    */
    @ApiModelProperty(value = "url路径", dataType = "String")
    @TableField("`url_path`")
    private String urlPath;

    /**
    * 原文件名
    */
    @ApiModelProperty(value = "原文件名", dataType = "String")
    @TableField("`name`")
    private String name;

    /**
    * 文件sha1
    */
    @ApiModelProperty(value = "文件sha1", dataType = "String")
    @TableField("`sha1`")
    private String sha1;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField("`create_time`")
    private Date createTime;

}
