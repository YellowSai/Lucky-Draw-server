package cn.marsLottery.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class UpdateInfoDTO {

    @Min(value = 1, message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", dataType = "int")
    private int id;

    @ApiModelProperty(value = "角色ID", dataType = "int")
    private int roleId;

    @NotEmpty(message = "用户昵称不能为空")
    @ApiModelProperty(value = "用户昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "头像地址", dataType = "String")
    private String avatar;

}
