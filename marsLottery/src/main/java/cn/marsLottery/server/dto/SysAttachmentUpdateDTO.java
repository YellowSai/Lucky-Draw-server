package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.SysAttachment;

    import java.util.Date;
/**
* 表[sys_attachment]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class SysAttachmentUpdateDTO {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
    * 存储路径
    */
    @ApiModelProperty(value = "存储路径", dataType = "String")
    private String path;

    /**
    * url路径
    */
    @ApiModelProperty(value = "url路径", dataType = "String")
    private String urlPath;

    /**
    * 原文件名
    */
    @ApiModelProperty(value = "原文件名", dataType = "String")
    private String name;

    /**
    * 文件sha1
    */
    @ApiModelProperty(value = "文件sha1", dataType = "String")
    private String sha1;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;
    public SysAttachment toSysAttachment() {
        SysAttachment sysAttachment = new SysAttachment();
        BeanUtil.copyProperties(this, sysAttachment);
        return sysAttachment;
    }
}
