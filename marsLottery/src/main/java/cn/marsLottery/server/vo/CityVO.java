package cn.marsLottery.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 表[sys_city]对应VO类
 *
 * @author auto
 */

@Data
public class CityVO {

    /**
     * 国内城市
     */
    @ApiModelProperty(value = "国内城市", dataType = "SysCityVO")
    private SysCityVO china;

    /**
     * 港澳台
     */
    @ApiModelProperty(value = "港澳台", dataType = "SysCityVO")
    private SysCityVO specialDistrict;

    /**
     * 国外城市
     */
    @ApiModelProperty(value = "国外城市", dataType = "SysCityVO")
    private SysCityVO foreignCity;

    /**
     * 热门城市
     */
    @ApiModelProperty(value = "热门城市", dataType = "SysCityVO")
    private SysCityVO hotCity;

    /**
     * 当前定位城市
     */
    @ApiModelProperty(value = "当前定位城市", dataType = "SysCityVO")
    private SysCityVO city;

}
