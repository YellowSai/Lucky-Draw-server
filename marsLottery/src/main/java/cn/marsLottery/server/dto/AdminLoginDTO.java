package cn.marsLottery.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class AdminLoginDTO {

    @ApiModelProperty(value = "用户名", dataType = "String", required = true)
    @NotEmpty(message = "用户名不可为空")
    private String username;

    @ApiModelProperty(value = "密码", dataType = "String", required = true)
    @Length(min = 6, max = 18, message = "密码长度不能小于6位并大于18位")
    private String password;

}
