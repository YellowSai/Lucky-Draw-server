package cn.marsLottery.server.controller.api;

import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.po.Prize;
import cn.marsLottery.server.service.PrizeService;
import cn.marsLottery.server.vo.PrizeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 表[prize]对应的前台类
 *
 * @author auto
 *
 */

@Slf4j
@RestController
@RequestMapping("/api/prize")
@Api(tags = {"抽奖奖品接口"})
@ApiAuth(ignore = true)
public class PrizeController {

    @Autowired
    private PrizeService prizeService;
    @ApiOperation(value = "抽奖奖品-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryId", value = "活动id", required = true, dataType = "int"),
    })
    @GetMapping("/list")
    public Result<List<PrizeVO>> list(@RequestParam(defaultValue = "0", required = false) int lotteryId) {
        List<Prize> prizeList = prizeService.getListByLotteryId(lotteryId);
        List<PrizeVO> prizeVOList = new ArrayList<>();
        for (Prize prize : prizeList){
            prizeVOList.add(new PrizeVO(prize));
        }
        return Result.data(prizeVOList);
    }
}
