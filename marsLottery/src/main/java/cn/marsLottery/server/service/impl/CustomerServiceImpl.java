package cn.marsLottery.server.service.impl;

import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.commons.util.ExcelUtil;
import cn.marsLottery.server.dao.CustomerDao;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryCustomerService;
import cn.marsLottery.server.service.LotteryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Customer对应的服务类实现
 *
 * @author auto
 */

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements CustomerService {

    @Autowired
    private LotteryCustomerService lotteryCustomerService;

    @Autowired
    private LotteryService lotteryService;

    @Override
    public void importExcel(File excel, int lotteryId) {
        Lottery lottery = lotteryService.getById(lotteryId);
        if (lottery == null) {
            throw new JzRuntimeException("活动不存在");
        }
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excel);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int length = sheet.getLastRowNum();
            if (length < 1) {
                throw new JzRuntimeException("excel文件为空");
            }

            Map<String, Map<String, String>> map = new HashMap<>();

            for (int i = 1; i <= length; i++) {
                String name =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(6)).equals("") ? "" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(6));
                String directorName =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(7)).equals("") ? "" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(7));
                String mobile = ExcelUtil.getStringValue(sheet.getRow(i).getCell(8)).equals("") ? "" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(8));
                String drawsTimes =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(9)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(9));
                String firstPrize =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(11)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(11));
                String secondPrize =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(12)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(12));
                String thirdPrize =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(13)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(13));
                String fourthPrize =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(14)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(14));
                String notPrize =  ExcelUtil.getStringValue(sheet.getRow(i).getCell(15)).equals("") ? "0" : ExcelUtil.getStringValue(sheet.getRow(i).getCell(15));

                String customerNumber = ExcelUtil.getStringValue(sheet.getRow(i).getCell(5));
                if (Objects.equals(customerNumber, "")){
                    throw new JzRuntimeException("第 " + (i + 1) +" 行，客户编号为空，导入取消");
                }
                if (Objects.equals(name, "")) {
                    throw new JzRuntimeException("第 " + (i + 1) +" 行，客户姓名为空，导入取消");
                }
                if (Objects.equals(directorName, "")) {
                    throw new JzRuntimeException("第 " + (i + 1) +" 行，经销商负责人姓名为空，导入取消");
                }
                if (Objects.equals(mobile, "")) {
                    throw new JzRuntimeException("第 " + (i + 1) +" 行，经销商负责人手机号，导入取消");
                }
                int total = Integer.parseInt(firstPrize) +
                        Integer.parseInt(secondPrize) +
                        Integer.parseInt(thirdPrize) +
                        Integer.parseInt(fourthPrize) +
                        Integer.parseInt(notPrize);
                if (total != Integer.parseInt(drawsTimes)) {
                    throw new JzRuntimeException("第 " + (i + 1) +" 行，总抽奖次数与奖项可中奖次数不匹配，导入取消");
                }

                Map<String, String> getData = new HashMap<>();
                getData.put("name", name);
                getData.put("directorName",directorName);
                getData.put("mobile",mobile);
                getData.put("drawsTimes",drawsTimes);
                getData.put("firstPrize",firstPrize);
                getData.put("secondPrize",secondPrize);
                getData.put("thirdPrize",thirdPrize);
                getData.put("fourthPrize",fourthPrize);
                getData.put("notPrize",notPrize);
                map.put(customerNumber, getData);
            }
            this.initRowData(map, lotteryId);
        } catch (Exception e) {
            throw new JzRuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Customer> listByIdIn(List<Integer> customerIdList) {
        if (customerIdList == null || customerIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<Customer> customerList = listByIds(customerIdList);
        return customerList;
    }

    @Override
    public Customer getByNumber(String number) {
        return this.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getNumber, number));
    }

    @Override
    public Map<Integer, Customer> getCustomerMap(List<Integer> customerIdList) {
        if (customerIdList == null || customerIdList.size() == 0) {
            return new HashMap<>();
        }
        List<Customer> customerList = listByIds(customerIdList);
        return customerList.stream().collect(Collectors.toMap(Customer::getId, a -> a));
    }

    @Override
    public Customer getByMobile(String mobile) {
        return this.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getMobile, mobile));
    }

    @Override
    public Customer getByCustomerNumberAndMobile(String customerNumber, String mobile) {
        return this.getOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getNumber, customerNumber)
                .eq(Customer::getMobile, mobile));
    }

    private void initRowData(Map<String, Map<String, String>> map, int lotteryId) {
        map.forEach((key, value) -> {
            Customer customer = this.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getNumber, key));
            LotteryCustomer lotteryCustomer;
            if (customer == null) {
                customer = new Customer();
                customer.setNumber(key);
                customer.setName(value.get("name"));
                customer.setDirectorName(value.get("directorName"));
                customer.setMobile(value.get("mobile"));
                this.save(customer);
                customer = this.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getNumber, key));
            } else {
                lotteryCustomer = lotteryCustomerService.getOne(new LambdaQueryWrapper<LotteryCustomer>().
                        eq(LotteryCustomer::getLotteryId, lotteryId).
                        eq(LotteryCustomer::getCustomerId, customer.getId()));
                if (lotteryCustomer != null){
                    throw new JzRuntimeException("编号 " + customer.getNumber() + " 在本活动中已存在，导入已中断");
                }
                customer.setNumber(key);
                customer.setName(value.get("name"));
                customer.setDirectorName(value.get("directorName"));
                customer.setMobile(value.get("mobile"));
                this.updateById(customer);
            }

            lotteryCustomer = new LotteryCustomer();
            lotteryCustomer.setLotteryId(lotteryId);
            lotteryCustomer.setCustomerId(customer.getId());
            lotteryCustomer.setDrawsTimes(Integer.parseInt(value.get("drawsTimes")));
            lotteryCustomer.setFirstPrize(Integer.parseInt(value.get("firstPrize")));
            lotteryCustomer.setSecondPrize(Integer.parseInt(value.get("secondPrize")));
            lotteryCustomer.setThirdPrize(Integer.parseInt(value.get("thirdPrize")));
            lotteryCustomer.setFourthPrize(Integer.parseInt(value.get("fourthPrize")));
            lotteryCustomer.setNotPrize(Integer.parseInt(value.get("notPrize")));

            if (lotteryCustomer.getId() == 0) {
                lotteryCustomerService.save(lotteryCustomer);
            }
            else {
                lotteryCustomerService.updateById(lotteryCustomer);
            }
        });
    }
}
