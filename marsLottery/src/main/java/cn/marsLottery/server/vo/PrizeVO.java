package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.Prize;

    import java.util.Date;
import lombok.Data;

/**
* 表[prize]对应VO类
*
* @author auto
*
*/

@Data
public class PrizeVO {

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

    public PrizeVO(Prize prize) {
        if (prize != null) {
            BeanUtil.copyProperties(prize, this);
        }
    }
}
