package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.Lottery;

    import java.util.Date;
import lombok.Data;

/**
* 表[lottery]对应VO类
*
* @author auto
*
*/

@Data
public class LotteryVO {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
    * 活动名称
    */
    @ApiModelProperty(value = "活动名称", dataType = "String")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty(value = "描述", dataType = "String")
    private String description;

    /**
     * 进货时间
     */
    @ApiModelProperty(value = "进货时间", dataType = "String")
    private String purchaseTime;

    /**
    * 开始时间
    */
    @ApiModelProperty(value = "开始时间", dataType = "Date")
    private Date startTime;

    /**
    * 结束时间
    */
    @ApiModelProperty(value = "结束时间", dataType = "Date")
    private Date endTime;

    /**
    * 活动是否开启
    */
    @ApiModelProperty(value = "活动是否开启", dataType = "String")
    private String isStart;

    /**
    * 活动说明
    */
    @ApiModelProperty(value = "活动说明", dataType = "String")
    private String explain;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    private int dataStatus;

    public LotteryVO(Lottery lottery) {
        if (lottery != null) {
            BeanUtil.copyProperties(lottery, this);
        }
    }
}
