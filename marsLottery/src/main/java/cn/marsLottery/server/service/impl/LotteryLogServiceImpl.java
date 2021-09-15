package cn.marsLottery.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.jzcscw.commons.constant.YesNoStatus;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.dao.LotteryLogDao;
import cn.marsLottery.server.po.*;
import cn.marsLottery.server.service.*;
import cn.marsLottery.server.vo.CustomerVO;
import cn.marsLottery.server.vo.LotteryLogVO;
import cn.marsLottery.server.vo.LotteryTotalDetailVO;
import cn.marsLottery.server.vo.PrizeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LotteryLog对应的服务类实现
 *
 * @author auto
 */

@Service
public class LotteryLogServiceImpl extends ServiceImpl<LotteryLogDao, LotteryLog> implements LotteryLogService {

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryCustomerService lotteryCustomerService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private SysCityService sysCityService;

    @Override
    public List<LotteryLog> getListByLotteryIdAndCustomerId(int lotteryId, int customerId) {
        return this.list(new LambdaQueryWrapper<LotteryLog>()
                .eq(LotteryLog::getCustomerId, customerId)
                .eq(LotteryLog::getLotteryId, lotteryId)
                .orderByDesc(LotteryLog::getId)
        );
    }

    @Override
    public List<LotteryLog> getListByLotteryId(int lotteryId) {
        return this.list(new LambdaQueryWrapper<LotteryLog>()
                .eq(LotteryLog::getLotteryId, lotteryId));
    }

    @Override
    public List<LotteryLog> getListByLotteryIdAndCustomerIdAndWinner(int lotteryId, int customerId) {
        if (lotteryId == 0){
            return this.list(new LambdaQueryWrapper<LotteryLog>()
                    .eq(LotteryLog::getCustomerId, customerId)
                    .eq(LotteryLog::getIsWinner, "Y")
                    .orderByDesc(LotteryLog::getId)
            );
        }
        return this.list(new LambdaQueryWrapper<LotteryLog>()
                .eq(LotteryLog::getCustomerId, customerId)
                .eq(LotteryLog::getLotteryId, lotteryId)
                .eq(LotteryLog::getIsWinner, "Y")
                .orderByDesc(LotteryLog::getId)
        );
    }

    @Override
    public LotteryLog getByLotteryIdAndCustomerIdAndWinner(int lotteryId, int customerId, String isWinner) {
        return this.getOne(new LambdaQueryWrapper<LotteryLog>()
                .eq(LotteryLog::getCustomerId, customerId)
                .eq(LotteryLog::getLotteryId, lotteryId)
                .eq(LotteryLog::getIsWinner, isWinner)
                .orderByDesc(LotteryLog::getId)
                .last("limit 1")
        );

    }

    @Override
    public List<LotteryLogVO> toListVO(List<LotteryLog> lotteryLogs) {
        if (CollectionUtil.isEmpty(lotteryLogs)) {
            return new ArrayList<>();
        }

        Map<Integer, Prize> prizeMap = new HashMap<>();
        List<Integer> prizeIdList = lotteryLogs.stream().map(LotteryLog::getPrizeId).collect(Collectors.toList());
        if (prizeIdList.size() > 0) {
            prizeMap = prizeService.getPrizeMap(prizeIdList);
        }

        Map<Integer, Customer> customerMap = new HashMap<>();
        List<Integer> customerIdList = lotteryLogs.stream().map(LotteryLog::getCustomerId).collect(Collectors.toList());
        if (customerIdList.size() > 0) {
            customerMap = customerService.getCustomerMap(customerIdList);
        }

        List<LotteryLogVO> lotteryLogVOList = new ArrayList<>();
        for (LotteryLog lotteryLog : lotteryLogs) {
            LotteryLogVO lotteryLogVO = new LotteryLogVO(lotteryLog);
            Prize prize = prizeMap.get(lotteryLog.getPrizeId());
            Customer customer = customerMap.get(lotteryLog.getCustomerId());
            lotteryLogVO.setPrize(new PrizeVO(prize));
            lotteryLogVO.setCustomerVO(new CustomerVO(customer));
            lotteryLogVOList.add(lotteryLogVO);
        }
        return lotteryLogVOList;
    }

    @Override
    public Page<LotteryLog> pageBy(int lotteryId, String isWinner, String number, String isDispatched, Page page) {
        return this.getBaseMapper().pageBy(lotteryId, isWinner, number, isDispatched, page);
    }

    @Override
    public String exportExcel(int lotteryId) throws IOException {
        Lottery lottery = lotteryService.getById(lotteryId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(new Timestamp(lottery.getStartTime().getTime()));
        String endTime = sdf.format(new Timestamp(lottery.getEndTime().getTime()));
        String current = sdf.format(new Timestamp(System.currentTimeMillis()));

        List<Object> list = activityProgress(lottery.getStartTime(), lottery.getEndTime(), lotteryId);

        File file = new File(appConfig.getUploadBasePath() + "/modelFile/lotteryLog.xlsx");

        FileInputStream f = new FileInputStream(file);  //XSL   XSLX

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheet("Sheet0");

            XSSFRow row1 = sheet.getRow(1);
            row1.getCell(1).setCellValue(lottery.getName());

            XSSFRow row2 = sheet.getRow(2);
            row2.getCell(1).setCellValue(startTime + "-" + endTime);

            XSSFRow row3 = sheet.getRow(3);
            row3.getCell(1).setCellValue(current);

            XSSFRow row4 = sheet.getRow(4);
            row4.getCell(1).setCellValue((String) list.get(0));

            XSSFRow row5 = sheet.getRow(8);
            row5.getCell(1).setCellValue((int) list.get(1));
            row5.getCell(2).setCellValue((int) list.get(2));
            row5.getCell(3).setCellValue((String) list.get(4));
            row5.getCell(4).setCellValue((int) list.get(3));

            XSSFRow row6 = sheet.getRow(9);
            row6.getCell(1).setCellValue((int) list.get(5));
            row6.getCell(2).setCellValue((int) list.get(6));
            row6.getCell(3).setCellValue((String) list.get(8));
            row6.getCell(4).setCellValue((int) list.get(7));

            XSSFRow row7 = sheet.getRow(13);
            row7.getCell(1).setCellValue((int) list.get(9));
            row7.getCell(2).setCellValue((int) list.get(10));
            row7.getCell(3).setCellValue(list.get(11) + "%");
            row7.getCell(4).setCellValue((int) list.get(12));
            row7.getCell(5).setCellValue((int) list.get(13));
            row7.getCell(6).setCellValue((int) list.get(14));
            row7.getCell(7).setCellValue((int) list.get(15));

            XSSFRow row8 = sheet.getRow(14);
            row8.getCell(1).setCellValue((int) list.get(16));
            row8.getCell(2).setCellValue((int) list.get(17));
            row8.getCell(3).setCellValue(list.get(18) + "%");
            row8.getCell(4).setCellValue((int) list.get(19));
            row8.getCell(5).setCellValue((int) list.get(20));
            row8.getCell(6).setCellValue((int) list.get(21));
            row8.getCell(7).setCellValue((int) list.get(22));

            XSSFRow row9 = sheet.getRow(15);
            row9.getCell(1).setCellValue((int) list.get(23));
            row9.getCell(2).setCellValue((int) list.get(24));
            row9.getCell(3).setCellValue(list.get(25) + "%");
            row9.getCell(4).setCellValue((int) list.get(26));
            row9.getCell(5).setCellValue((int) list.get(27));
            row9.getCell(6).setCellValue((int) list.get(28));
            row9.getCell(7).setCellValue((int) list.get(29));

            XSSFRow row10 = sheet.getRow(16);
            row10.getCell(1).setCellValue((int) list.get(30));
            row10.getCell(2).setCellValue((int) list.get(31));
            row10.getCell(3).setCellValue(list.get(32) + "%");
            row10.getCell(4).setCellValue((int) list.get(33));
            row10.getCell(5).setCellValue((int) list.get(34));
            row10.getCell(6).setCellValue((int) list.get(35));
            row10.getCell(7).setCellValue((int) list.get(36));

            int row21 = 21;
            for (int i = 37; i < list.size(); i = i + 3) {
                XSSFRow row11 = sheet.createRow((short)(row21++)); //在现有行号后追加数据
                row11.createCell(0).setCellValue((String) list.get(i)); //设置第一个（从0开始）单元格的数据
                row11.createCell(1).setCellValue((String) list.get(i + 1));
                row11.createCell(2).setCellValue((String) list.get(i + 2));

            }

            String filePrefix = UUID.randomUUID().toString();
            String newFileName = filePrefix + ".xlsx";
            String absolutePath = appConfig.getUploadBasePath() + "/modelFile/" + newFileName;
            String fileUrl = "/modelFile/" + newFileName;

            FileOutputStream outputStream = new FileOutputStream(absolutePath);

            workbook.write(outputStream);
            outputStream.close();       //关闭

            return fileUrl;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    @Override
    public LotteryTotalDetailVO eventsGallery(int lotteryLogId) {
        Lottery lottery = lotteryService.getById(lotteryLogId);
        List<Object> receive = activityProgress(lottery.getStartTime(), lottery.getEndTime(), lotteryLogId);

        LotteryTotalDetailVO totalDetailVO = new LotteryTotalDetailVO();

        totalDetailVO.setName(lottery.getName());
        totalDetailVO.setStartTime(lottery.getStartTime());
        totalDetailVO.setEndTime(lottery.getEndTime());
        totalDetailVO.setCurrentTime(new Date());
        totalDetailVO.setSchedule((String) receive.get(0));

        List<Map<String, Object>> overallSituation = new ArrayList<>();
        for (int i = 1, count = 0; count < 2; count++) {
            int localRow = i + count * 4;
            Map<String, Object> map = new HashMap<>();
            map.put("today", receive.get(localRow));
            map.put("yesterday", receive.get(localRow + 1));
            map.put("total", receive.get(localRow + 2));
            map.put("dailyAverage", receive.get(localRow + 3));
            overallSituation.add(map);
        }
        totalDetailVO.setOverallSituation(overallSituation);

        List<Map<String, Object>> winningSituation = new ArrayList<>();
        for (int i = 9, count = 0; count < 4; count++) {
            int localRow = i + count * 7;
            Map<String, Object> map = new HashMap<>();
            map.put("today",receive.get(localRow));
            map.put("yesterday",receive.get(localRow + 1));
            map.put("prizeProbability",receive.get(localRow + 2));
            map.put("prizeNumber",receive.get(localRow + 3));
            map.put("prizeShipped",receive.get(localRow + 4));
            map.put("prizeTotal",receive.get(localRow + 5));
            map.put("prizeRemaining",receive.get(localRow + 6));
            winningSituation.add(map);
        }
        totalDetailVO.setWinningSituation(winningSituation);


        List<Map<String, Object>> todayWinningSituation = new ArrayList<>();
        for (int i = 37; i < receive.size(); i = i + 3) {
            Map<String, Object> map = new HashMap<>();
            map.put("openId",receive.get(i));
            map.put("award",receive.get(i + 1));
            map.put("isFill",receive.get(i + 2));
            todayWinningSituation.add(map);
        }
        totalDetailVO.setTodayWinningSituation(todayWinningSituation);


        return totalDetailVO;
    }


    private List<Object> activityProgress(Date startTimes, Date endTimes, int lotteryId) {
        List<Object> list = new ArrayList<>();

        //时间
        long current = new Timestamp(System.currentTimeMillis()).getTime();
        long startTime = new Timestamp(startTimes.getTime()).getTime();
        long endTime = new Timestamp(endTimes.getTime()).getTime();

        long days = current - startTime;

        long totalDays = endTime - startTime;

        double activityProgress = days * 1.0 / totalDays;

        if (activityProgress > 1) {
            activityProgress = 1;
        }

        if (activityProgress < 0) {
            activityProgress = 0;
        }

        DecimalFormat df = new DecimalFormat("0.00%");
        list.add(df.format(activityProgress));

        DecimalFormat format = new DecimalFormat("0.00");

        Date yesterday = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        Date tomorrow = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(tomorrow);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date tomorrowZero = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(yesterday);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date yesterdayZero = calendar.getTime();

        //活动整体参与概况
        List<LotteryLog> todayLottery = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsWinner, YesNoStatus.YES.getValue()).ge(LotteryLog::getCreateTime, zero).le(LotteryLog::getCreateTime, tomorrowZero));
        list.add(todayLottery.size());

        List<LotteryLog> yesterdayLottery = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsWinner, YesNoStatus.YES.getValue()).ge(LotteryLog::getCreateTime, yesterdayZero).le(LotteryLog::getCreateTime, zero));
        list.add(yesterdayLottery.size());

        List<LotteryLog> totalLottery = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsWinner, YesNoStatus.YES.getValue()));
        list.add(totalLottery.size());

        if (days <= 0) {
            days = 1;
        }

        if (totalLottery.size() > 0) {
            double day = days < totalDays ? days * 1.0 / 86400000 : totalDays * 1.0 / 86400000;
            double dailyAverageLottery = totalLottery.size() * 1.0 / Math.ceil(day);
            list.add(String.format("%.1f",dailyAverageLottery));
        }else {
            list.add("0.0");
        }

        List<LotteryLog> todayFillIn = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsFill, YesNoStatus.YES.getValue()).ge(LotteryLog::getCreateTime, zero).le(LotteryLog::getCreateTime, tomorrowZero));
        list.add(todayFillIn.size());

        List<LotteryLog> yesterdayFillIn = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsFill, YesNoStatus.YES.getValue()).ge(LotteryLog::getCreateTime, yesterdayZero).le(LotteryLog::getCreateTime, zero));
        list.add(yesterdayFillIn.size());

        List<LotteryLog> totalFillI = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsFill, YesNoStatus.YES.getValue()));
        list.add(totalFillI.size());

        if (totalFillI.size() > 0) {
            double day = days < totalDays ? days * 1.0 / 86400000 : totalDays * 1.0 / 86400000;
            double dailyAverageFillIn = totalFillI.size() * 1.0 / Math.ceil(day);
            list.add(String.format("%.1f",dailyAverageFillIn));
        }else {
            list.add("0.0");
        }

        //活动PX中奖概况
        List<LotteryCustomer> lotteryNumber = lotteryCustomerService.list(new LambdaQueryWrapper<LotteryCustomer>().eq(LotteryCustomer::getLotteryId, lotteryId));

        int[] prizeTotal = {0,0,0,0};
        for (LotteryCustomer lotteryCustomer : lotteryNumber) {
            prizeTotal[0] += lotteryCustomer.getFirstPrize();
            prizeTotal[1] += lotteryCustomer.getSecondPrize();
            prizeTotal[2] += lotteryCustomer.getThirdPrize();
            prizeTotal[3] += lotteryCustomer.getFourthPrize();
        }

        int[] todayAward = {0,0,0,0};

        for (LotteryLog todayInteger : todayLottery) {
            Prize todayPrize = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId,todayInteger.getPrizeId()));
            todayAward[todayPrize.getAward() - 1] += 1;
        }
        list.add(todayAward[0]);

        int[] yesterdayAward = {0,0,0,0};
         for (LotteryLog yesterdayInteger : yesterdayLottery) {
            Prize yesterdayPrizes = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId,yesterdayInteger.getPrizeId()));
            yesterdayAward[yesterdayPrizes.getAward() - 1] += 1;
        }
        list.add(yesterdayAward[0]);

        int[] awardNumber = {0,0,0,0};
         for (LotteryLog prizeInteger : totalLottery) {
            Prize yesterdayPrizes = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId,prizeInteger.getPrizeId()));
            awardNumber[yesterdayPrizes.getAward() - 1] += 1;
        }

        if (prizeTotal[0] != 0){
            double firstPrizeProbability = (awardNumber[0] * 1.0 / prizeTotal[0]) * 100;  //11
            if (firstPrizeProbability > 100){
                list.add("100.00");
            }else {
                list.add(format.format(firstPrizeProbability));
            }
        }else {
            list.add("0.0");
        }

        list.add(awardNumber[0]);  //12

        int[] prizeShipped = {0, 0, 0, 0};
        List<LotteryLog> lotteryShipped = list(new LambdaQueryWrapper<LotteryLog>().
                eq(LotteryLog::getLotteryId, lotteryId).eq(LotteryLog::getIsDispatched, 'Y'));
        if (lotteryShipped.size() != 0) {
            for (LotteryLog lotteryLog : lotteryShipped) {
                Prize prize = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId, lotteryLog.getPrizeId()));
                prizeShipped[prize.getAward() - 1] += 1;
            }
        }

        list.add(prizeShipped[0]);

        list.add(prizeTotal[0]);
        int firstPrizeRemaining = prizeTotal[0] - awardNumber[0];
        list.add(Math.max(firstPrizeRemaining, 0));

        list.add(todayAward[1]);

        list.add(yesterdayAward[1]);

        if (prizeTotal[1] != 0){
            double secondPrizeProbability = (awardNumber[1] * 1.0 / prizeTotal[1]) * 100;
            if (secondPrizeProbability > 100){
                list.add("100.00");
            }else {
                list.add(format.format(secondPrizeProbability));
            }
        }else {
            list.add("0.0");
        }

        list.add(awardNumber[1]);

        list.add(prizeShipped[1]);

        list.add(prizeTotal[1]);
        int secondPrizeRemaining = prizeTotal[1] - awardNumber[1];
        if (secondPrizeRemaining < 0) {
            list.add(0);
        }else {
            list.add(secondPrizeRemaining);
        }

        list.add(todayAward[2]);

        list.add(yesterdayAward[2]);

        if (prizeTotal[2] != 0){
            double thirdPrizeProbability = (awardNumber[2] * 1.0 / prizeTotal[2]) * 100;
            if (thirdPrizeProbability > 100){
                list.add("100.00");
            }else {
                list.add(format.format(thirdPrizeProbability));
            }
        } else {
            list.add("0.0");
        }

        list.add(awardNumber[2]);

        list.add(prizeShipped[2]);

        list.add(prizeTotal[2]);
        int thirdPrizeRemaining = prizeTotal[2] - awardNumber[2];
        if (thirdPrizeRemaining < 0){
            list.add(0);
        }else {
            list.add(thirdPrizeRemaining);
        }

        list.add(todayAward[3]);

        list.add(yesterdayAward[3]);

        if (prizeTotal[3] != 0){
            double fourthPrizeProbability = (awardNumber[3] * 1.0 / prizeTotal[3]) * 100;
            if (fourthPrizeProbability > 100){
                list.add("100.00");
            }else {
                list.add(format.format(fourthPrizeProbability));
            }
        }else {
            list.add("0.0");
        }

        list.add(awardNumber[3]);

        list.add(prizeShipped[3]);

        list.add(prizeTotal[3]);
        int fourthPrizeRemaining = prizeTotal[3] - awardNumber[3];
        if (fourthPrizeRemaining < 0){
            list.add(0);
        } else {
            list.add(fourthPrizeRemaining);
        }

        if (todayLottery.size() > 0) {
            for (LotteryLog lotteryLog : todayLottery) {
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getCustomerId, lotteryLog.getCustomerId()));
                list.add(user.getOpenId());
                Prize prize = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId, lotteryLog.getPrizeId()));
                switch (prize.getAward()) {
                    case 1:
                        list.add("一等奖");
                        break;
                    case 2:
                        list.add("二等奖");
                        break;
                    case 3:
                        list.add("三等奖");
                        break;
                    case 4:
                        list.add("四等奖");
                        break;
                }
                list.add(lotteryLog.getIsFill());
            }
        } else {
            list.add("");
            list.add("");
            list.add("");
        }
        return list;
    }

    @Override
    public String exportReceiptInformation(int lotteryId) throws IOException {

        List<Object> list = receiptInformation(lotteryId);

        File file = new File(appConfig.getUploadBasePath() + "/modelFile/receivingInformation.xlsx");

        FileInputStream f = new FileInputStream(file);  //XSL   XSLX

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheet("Sheet0");

            int row = 1;
            for (int i = 0; i < list.size(); i = i + 10) {
                XSSFRow xssfRow = sheet.createRow((short)(row++)); //在现有行号后追加数据

                xssfRow.createCell(0).setCellValue((String) list.get(i));
                xssfRow.createCell(1).setCellValue((String) list.get(i + 1));
                xssfRow.createCell(2).setCellValue((String) list.get(i + 2));
                xssfRow.createCell(3).setCellValue((String) list.get(i + 3));
                xssfRow.createCell(4).setCellValue((String) list.get(i + 4));
                xssfRow.createCell(5).setCellValue((String) list.get(i + 5));
                xssfRow.createCell(6).setCellValue((String) list.get(i + 6));
                xssfRow.createCell(7).setCellValue((String) list.get(i + 7));
                xssfRow.createCell(8).setCellValue((String) list.get(i + 8));
                xssfRow.createCell(9).setCellValue((String) list.get(i + 9));
            }

            String filePrefix = UUID.randomUUID().toString();
            String newFileName = filePrefix + ".xlsx";
            String absolutePath = appConfig.getUploadBasePath() + "/modelFile/" + newFileName;
            String fileUrl = "/modelFile/" + newFileName;

            FileOutputStream outputStream = new FileOutputStream(absolutePath);

            workbook.write(outputStream);
            outputStream.close();       //关闭

            return fileUrl;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    private List<Object> receiptInformation(int lotteryId) {
        List<Object> list = new ArrayList<>();

        List<LotteryLog> todayLottery = list(new LambdaQueryWrapper<LotteryLog>().eq(LotteryLog::getLotteryId, lotteryId).
                eq(LotteryLog::getIsWinner, YesNoStatus.YES.getValue()).eq(LotteryLog::getIsFill, YesNoStatus.YES.getValue()));

        for (LotteryLog lotteryLog : todayLottery) {
            Customer customer = customerService.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getId, lotteryLog.getCustomerId()));

            list.add(customer.getNumber());
            list.add(customer.getDirectorName());
            list.add(customer.getMobile());

            Prize prize = prizeService.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getId, lotteryLog.getPrizeId()));
            switch (prize.getAward()) {
                case 1:
                    list.add("一等奖");
                    break;
                case 2:
                    list.add("二等奖");
                    break;
                case 3:
                    list.add("三等奖");
                    break;
                case 4:
                    list.add("四等奖");
                    break;
            }

            Receipt receipt = receiptService.getOne(new LambdaQueryWrapper<Receipt>().eq(Receipt::getLotteryLogId, lotteryLog.getId()));
            list.add(receipt.getRecipient());
            list.add(receipt.getMobile());

            String city = null;

            SysCity firstLevel = sysCityService.getOne(new LambdaQueryWrapper<SysCity>().eq(SysCity::getId,receipt.getCityId()));
            if (firstLevel != null && firstLevel.getParentId() != 10000000000L) {
                SysCity secondLevel = sysCityService.getOne(new LambdaQueryWrapper<SysCity>().eq(SysCity::getId,firstLevel.getParentId()));
                if (secondLevel != null && secondLevel.getParentId() != 10000000000L) {
                    SysCity thirdLevel = sysCityService.getOne(new LambdaQueryWrapper<SysCity>().eq(SysCity::getId,secondLevel.getParentId()));
                    if (thirdLevel != null && thirdLevel.getParentId() != 10000000000L) {
                        city = thirdLevel.getName() + "-" + secondLevel.getName() + "-" + firstLevel.getName();
                    } else {
                        city = secondLevel.getName() + "-" + firstLevel.getName();
                    }
                } else {
                    city = firstLevel.getName();
                }
            }else {
                city = firstLevel.getName();
            }
            list.add(city);

            list.add(receipt.getAddress());
            list.add(receipt.getDeliveryNum());
            list.add(receipt.getDeliveryName());
        }

        return list;
    }

}
