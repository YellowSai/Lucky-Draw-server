package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.PrizeAddDTO;
import cn.marsLottery.server.dto.PrizeUpdateDTO;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.po.Prize;
import cn.marsLottery.server.service.PrizeService;
import cn.marsLottery.server.vo.PrizeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表[prize]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/prize")
@Api(tags = {"抽奖奖品管理接口"})
public class PrizeAdminController {

    @Autowired
    private PrizeService prizeService;

    @ApiOperation(value = "抽奖奖品-列表")
    @GetMapping("/list")
    public Result<Page<Prize>> list(@RequestParam(defaultValue = "0", required = false) int lotteryId,
                                    Page page) {
        LambdaQueryWrapper<Prize> queryWrapper = new LambdaQueryWrapper<>();
        if (lotteryId > 0) {
            queryWrapper.eq(Prize::getLotteryId,lotteryId);
        }

        Page<Prize> pageData = prizeService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "抽奖奖品-详情")
    @GetMapping("/detail")
    public Result<PrizeVO> detail(int id) {
        if (id <= 0) {
            return Result.error("抽奖奖品不存在");
        }
        Prize prize = prizeService.getById(id);
        if (prize == null) {
            return Result.error("抽奖奖品不存在");
        }
        PrizeVO prizeVO = new PrizeVO(prize);
        return Result.data(prizeVO);
    }

    @ApiOperation(value = "抽奖奖品-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated PrizeAddDTO prizeAddDTO) {
        Prize prizeQuery = prizeService.getByLotteryIdAndAward(prizeAddDTO.getLotteryId(), prizeAddDTO.getAward());
        if (prizeQuery != null) {
            return Result.error("当前奖项已存在，请选择其他奖项");
        }

        Prize prize = prizeAddDTO.toPrize();

        boolean temp = prizeService.save(prize);
        return temp ? Result.ok("奖品添加成功") : Result.error("奖品添加失败");
    }

    @ApiOperation(value = "抽奖奖品-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated PrizeUpdateDTO prizeUpdateDTO) {
        Prize oldPrize = prizeService.getById(prizeUpdateDTO.getId());
        if (oldPrize == null) {
            return Result.error("抽奖奖品不存在");
        }
        Prize prize = prizeUpdateDTO.toPrize();
        BeanUtils.copyProperties(prize, oldPrize);
        prizeService.updateById(prize);
        return Result.ok("编辑抽奖奖品成功");
    }

    @ApiOperation(value = "抽奖奖品-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("抽奖奖品不存在");
        }
        Prize prize = prizeService.getById(id);
        if (prize == null) {
            return Result.error("抽奖奖品不存在");
        }
        prizeService.removeById(id);
        return Result.ok("删除抽奖奖品成功");
    }
}
