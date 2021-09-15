package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.SysAttachment;

    import java.util.Date;
import lombok.Data;

/**
* 表[sys_attachment]对应VO类
*
* @author auto
*
*/

@Data
public class SysAttachmentVO {

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

    public SysAttachmentVO(SysAttachment sysAttachment) {
        if (sysAttachment != null) {
            BeanUtil.copyProperties(sysAttachment, this);
        }
    }
}
