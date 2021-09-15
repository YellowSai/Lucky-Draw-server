package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.LotteryLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class TokenVO {

    @ApiModelProperty(value = "客户Id", dataType = "int")
    private int customerId;

    @ApiModelProperty(value = "token", dataType = "String")
    private String token;

    public TokenVO() {

    }
}


