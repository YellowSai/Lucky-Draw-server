package cn.marsLottery.server.controller.admapi;

import cn.hutool.core.io.FileUtil;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.marsLottery.server.dto.CustomerAddDTO;
import cn.marsLottery.server.dto.CustomerUpdateDTO;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.SysAttachment;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.UploadService;
import cn.marsLottery.server.vo.CustomerVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import cn.jzcscw.commons.core.Result;
import org.apache.http.impl.io.IdentityInputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
* 表[customer]对应的后台管理类
*
* @author auto
*
*/

@Slf4j
@RestController
@RequestMapping("/admapi/customer")
@Api(tags = {"客户管理接口"})
public class CustomerAdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "客户-列表")
    @GetMapping("/list")
    public Result<Page<Customer>> list(Page page) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        //FIXME 根据需要增加搜索条件
        Page<Customer> pageData = customerService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "客户-详情")
    @GetMapping("/detail")
    public Result<CustomerVO> detail(int id) {
        if (id <= 0) {
            return Result.error("客户不存在");
        }
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        CustomerVO customerVO = new CustomerVO(customer);
        return Result.data(customerVO);
    }

    @ApiOperation(value = "客户-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated CustomerAddDTO customerAddDTO) {
        Customer customer = customerAddDTO.toCustomer();
        customerService.save(customer);
        return Result.ok("添加客户成功");
    }

    @ApiOperation(value = "客户-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated CustomerUpdateDTO customerUpdateDTO) {
        Customer oldCustomer = customerService.getById(customerUpdateDTO.getId());
        if (oldCustomer == null) {
            return Result.error("客户不存在");
        }
        Customer customer = customerUpdateDTO.toCustomer();
        BeanUtils.copyProperties(customer, oldCustomer);
        customerService.updateById(customer);
        return Result.ok("编辑客户成功");
    }

    @ApiOperation(value = "客户-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("客户不存在");
        }
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        customerService.removeById(id);
        return Result.ok("删除客户成功");
    }

    @ApiOperation(value = "导入客户-增加")
    @PostMapping("/importExcel")
    public Result<String> importExcel(String url, int lotteryId) {
        SysAttachment sysAttachment = uploadService.getSysAttachmentByUrl(url);
        if (sysAttachment == null) {
            return Result.error("上传文件丢失");
        }
        File excel = FileUtil.file(sysAttachment.getPath());
        if (excel == null) {
            return Result.error("上传文件丢失");
        }
        try {
            customerService.importExcel(excel, lotteryId);
            return Result.ok("导入成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
