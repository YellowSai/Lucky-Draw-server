package cn.marsLottery.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUserVO {

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id", dataType = "int")
    private int customerId;

    /**
     * token
     */
    @ApiModelProperty(value = "token", dataType = "String")
    private String token;

}
