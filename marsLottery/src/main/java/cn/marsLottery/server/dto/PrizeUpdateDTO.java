package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.Prize;

    import java.util.Date;
/**
* 表[prize]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class PrizeUpdateDTO {

    /**
    * 
    */
    @ApiModelProperty(value = "", dataType = "int")
    private int id;

    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", dataType = "int")
    private int lotteryId;

    /**
    * 奖品名称
    */
    @ApiModelProperty(value = "奖品名称", dataType = "String")
    private String name;

    /**
    * 奖品图片
    */
    @ApiModelProperty(value = "奖品图片", dataType = "String")
    private String image;

    /**
     * 几等奖
     */
    @ApiModelProperty(value = "几等奖", dataType = "int")
    @TableField("`award`")
    private int award;

    /**
    * 奖品描述
    */
    @ApiModelProperty(value = "奖品描述", dataType = "String")
    private String describe;

    /**
    * 奖品价值
    */
    @ApiModelProperty(value = "奖品价值", dataType = "Double")
    private Double price;

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
    public Prize toPrize() {
        Prize prize = new Prize();
        BeanUtil.copyProperties(this, prize);
        return prize;
    }
}
