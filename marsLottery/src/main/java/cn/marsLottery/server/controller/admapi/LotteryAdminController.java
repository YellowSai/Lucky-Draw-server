package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.LotteryAddDTO;
import cn.marsLottery.server.dto.LotteryUpdateDTO;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.service.LotteryService;
import cn.marsLottery.server.vo.LotteryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;


/**
 * 表[lottery]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/lottery")
@Api(tags = {"抽奖活动管理接口"})
public class LotteryAdminController {

    @Autowired
    private LotteryService lotteryService;

    @ApiOperation(value = "抽奖活动-列表")
    @GetMapping("/list")
    public Result<Page<Lottery>> list(@RequestParam(defaultValue = "", required = false) String keywords,
                                      Page page) {
        LambdaQueryWrapper<Lottery> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotEmpty(keywords)){
            queryWrapper.eq(Lottery::getName,keywords);
        }
        queryWrapper.orderByDesc(Lottery::getId);
        Page<Lottery> pageData = lotteryService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "抽奖活动-详情")
    @GetMapping("/detail")
    public Result<LotteryVO> detail(int id) {
        if (id <= 0) {
            return Result.error("抽奖活动不存在");
        }
        Lottery lottery = lotteryService.getById(id);
        if (lottery == null) {
            return Result.error("抽奖活动不存在");
        }

        LotteryVO lotteryVO = new LotteryVO(lottery);

        return Result.data(lotteryVO);
    }

    @ApiOperation(value = "抽奖活动-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated LotteryAddDTO lotteryAddDTO) {
        Lottery lottery = lotteryAddDTO.toLottery();
        lotteryService.save(lottery);
        return Result.ok("添加抽奖活动成功");
    }

    @ApiOperation(value = "抽奖活动-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated LotteryUpdateDTO lotteryUpdateDTO) {
        Lottery oldLottery = lotteryService.getById(lotteryUpdateDTO.getId());
        if (oldLottery == null) {
            return Result.error("抽奖活动不存在");
        }
        Lottery lottery = lotteryUpdateDTO.toLottery();
        BeanUtils.copyProperties(lottery, oldLottery);
        lotteryService.updateById(lottery);
        return Result.ok("编辑抽奖活动成功");
    }

    @ApiOperation(value = "抽奖活动-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("抽奖活动不存在");
        }
        Lottery lottery = lotteryService.getById(id);
        if (lottery == null) {
            return Result.error("抽奖活动不存在");
        }
        lotteryService.removeById(id);
        return Result.ok("删除抽奖活动成功");
    }
}
