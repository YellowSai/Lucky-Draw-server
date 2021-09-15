package cn.marsLottery.server.controller.api;

import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.po.SysCity;
import cn.marsLottery.server.service.SysCityService;
import cn.marsLottery.server.vo.CityVO;
import cn.marsLottery.server.vo.SysCityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/api/sysCity")
@Api(tags = {"省市区接口"})
@ApiAuth(ignore = true)
public class SysCityController {

    @Autowired
    private SysCityService sysCityService;

    @ApiOperation(value = "城市信息接口")
    @GetMapping("/detail")
    public Result<CityVO> detail(@RequestParam(defaultValue = "", required = false) String address) {
        CityVO cityVO = sysCityService.getCityVo(address);
        return Result.data(cityVO);
    }

    @ApiOperation(value = "城市树状结构")
    @GetMapping("/tree")
    public Result<List<SysCityVO>> tree(@RequestParam(defaultValue = "0", required = false) int grade) {
        List<SysCity> list = sysCityService.listByMaxGrade(grade);
        List<SysCityVO> cityTree = sysCityService.generateTree(list);
        return Result.data(cityTree);
    }

    @ApiOperation(value = "获取定位城市信息")
    @GetMapping("/get")
    public Result<SysCity> tree(@RequestParam(defaultValue = "", required = false) String address) {
        SysCity sysCity = sysCityService.getByAddress(address);
        return Result.data(sysCity);
    }
}
