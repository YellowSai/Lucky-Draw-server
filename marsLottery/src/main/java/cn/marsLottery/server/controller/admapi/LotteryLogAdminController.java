package cn.marsLottery.server.controller.admapi;

import cn.hutool.core.io.FileUtil;
import cn.jzcscw.commons.constant.YesNoStatus;
import cn.jzcscw.commons.util.StringUtil;
import cn.marsLottery.server.dto.LotteryLogAddDTO;
import cn.marsLottery.server.dto.LotteryLogUpdateDTO;
import cn.marsLottery.server.po.LotteryLog;
import cn.marsLottery.server.po.Prize;
import cn.marsLottery.server.po.SysAttachment;
import cn.marsLottery.server.service.LotteryLogService;
import cn.marsLottery.server.service.PrizeService;
import cn.marsLottery.server.vo.LotteryLogVO;
import cn.marsLottery.server.vo.LotteryTotalDetailVO;
import cn.marsLottery.server.vo.PrizeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import cn.jzcscw.commons.core.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表[lottery_log]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/lotteryLog")
@Api(tags = {"抽奖记录管理接口"})
public class LotteryLogAdminController {

    @Autowired
    private LotteryLogService lotteryLogService;

    @ApiOperation(value = "抽奖记录-列表")
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0", required = false) int lotteryId,
                       @RequestParam(defaultValue = "", required = false) String isWinner,
                       @RequestParam(defaultValue = "", required = false) String number,
                       @RequestParam(defaultValue = "", required = false) String isDispatched,
                       Page page) {
        Page<LotteryLog> pageData = lotteryLogService.pageBy(lotteryId, isWinner, number, isDispatched, page);

        List<LotteryLogVO> lotteryLogVOList = lotteryLogService.toListVO(pageData.getRecords());

        Page<LotteryLogVO> result = new Page<>();
        result.setRecords(lotteryLogVOList);
        result.setTotal(pageData.getTotal());
        return Result.data(result);
    }

    @ApiOperation(value = "导出excel")
    @GetMapping("/exportExcel")
    public Result<String> exportExcel(int lotteryId) throws IOException {
        List<LotteryLog> lotteryLog = lotteryLogService.getListByLotteryId(lotteryId);
        if (lotteryLog.size() < 0) {
            return Result.error("活动不存在");
        }
        String url = lotteryLogService.exportExcel(lotteryId);
        return Result.data(url);
    }

    @ApiOperation(value = "展示抽奖活动明细")
    @GetMapping("/lotteryTotalDetail")
    public Result<LotteryTotalDetailVO> LotteryTotalDetail(int lotteryId) {
        List<LotteryLog> lotteryLog = lotteryLogService.getListByLotteryId(lotteryId);
        if (lotteryLog.size() < 0) {
            return Result.error("活动不存在");
        }
        LotteryTotalDetailVO lotteryTotalDetailVO = lotteryLogService.eventsGallery(lotteryId);
        return Result.data(lotteryTotalDetailVO);
    }

    @ApiOperation(value = "导出收货信息")
    @GetMapping("/exportReceiptInformation")
    public Result<String> exportReceiptInformationExcel(int lotteryId) throws IOException {
        List<LotteryLog> lotteryLog = lotteryLogService.getListByLotteryId(lotteryId);
        if (lotteryLog.size() < 0) {
            return Result.error("活动不存在");
        }
        String url = lotteryLogService.exportReceiptInformation(lotteryId);
        return Result.data(url);
    }
}
