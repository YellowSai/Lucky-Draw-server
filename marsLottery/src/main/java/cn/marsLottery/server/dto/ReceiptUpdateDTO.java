package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.Receipt;

    import java.util.Date;
/**
* 表[receipt]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class ReceiptUpdateDTO {

    /**
    * 
    */
    @ApiModelProperty(value = "", dataType = "int")
    private int id;

    /**
    * 抽奖记录id
    */
    @ApiModelProperty(value = "抽奖记录id", dataType = "int")
    private int lotteryLogId;

    /**
    * 收件人
    */
    @ApiModelProperty(value = "收件人", dataType = "String")
    private String recipient;

    /**
    * 手机号码
    */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    private String mobile;

    /**
     * 所在城市id
     */
    @ApiModelProperty(value = "所在城市id", dataType = "long")
    private long cityId;

    /**
    * 详细地址
    */
    @ApiModelProperty(value = "详细地址", dataType = "String")
    private String address;

    /**
    * 快递单号
    */
    @ApiModelProperty(value = "快递单号", dataType = "String")
    private String deliveryNum;

    /**
     * 快递公司
     */
    @ApiModelProperty(value = "快递公司", dataType = "String")
    private String deliveryName;

    /**
    * 发货时间
    */
    @ApiModelProperty(value = "发货时间", dataType = "Date")
    private Date dispatchedTime;

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
    public Receipt toReceipt() {
        Receipt receipt = new Receipt();
        BeanUtil.copyProperties(this, receipt);
        return receipt;
    }
}
