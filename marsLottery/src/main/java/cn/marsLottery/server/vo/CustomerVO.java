package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.Customer;

    import java.util.Date;
import lombok.Data;

/**
* 表[customer]对应VO类
*
* @author auto
*
*/

@Data
public class CustomerVO {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
    * 客户编号
    */
    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String number;

    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String name;

    /**
    * 负责人名称
    */
    @ApiModelProperty(value = "负责人名称", dataType = "String")
    private String directorName;

    /**
    * 手机号码
    */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    private String mobile;

//    /**
//    * 创建时间
//    */
//    @ApiModelProperty(value = "创建时间", dataType = "Date")
//    private Date createTime;
//
//    /**
//    * 通用状态,2正常,3删除
//    */
//    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
//    private int dataStatus;

    public CustomerVO(Customer customer) {
        if (customer != null) {
            BeanUtil.copyProperties(customer, this);
        }
    }
}
