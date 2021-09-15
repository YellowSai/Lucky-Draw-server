package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.constant.DataStatus;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.PinyinUtil;
import cn.marsLottery.server.dto.SysCityAddDTO;
import cn.marsLottery.server.dto.SysCityUpdateDTO;
import cn.marsLottery.server.po.SysCity;
import cn.marsLottery.server.service.SysCityService;
import cn.marsLottery.server.vo.SysCityVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表[sys_city]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/sysCity")
@Api(tags = {"省市区管理接口"})
public class SysCityAdminController {

    @Autowired
    private SysCityService sysCityService;

    @ApiOperation(value = "城市列表")
    @GetMapping("/list")
    public Result<Page<SysCityVO>> tree(@RequestParam(defaultValue = "", required = false) String parentId,
                                        Page page) {
        LambdaQueryWrapper<SysCity> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(SysCity::getParentId, parentId);
        userLambdaQueryWrapper.orderByAsc(SysCity::getId);
        Page<SysCity> pageData = sysCityService.page(page, userLambdaQueryWrapper);
        List<SysCityVO> sysCityVOList = pageData.getRecords().stream().map(SysCityVO::new).collect(Collectors.toList());
        Page<SysCityVO> result = new Page<>();
        result.setTotal(pageData.getTotal());
        result.setRecords(sysCityVOList);
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        return Result.data(result);
    }

    @ApiOperation(value = "省市区-详情")
    @GetMapping("/detail")
    public Result<SysCityVO> detail(int id) {
        if (id <= 0) {
            return Result.error("省市区不存在");
        }
        SysCity sysCity = sysCityService.getById(id);
        if (sysCity == null) {
            return Result.error("省市区不存在");
        }
        SysCityVO sysCityVO = new SysCityVO(sysCity);
        return Result.data(sysCityVO);
    }

    @ApiOperation(value = "省市区-增加")
    @PostMapping("/add")
    @OperateLog(description = "新增系统城市")
    public Result<String> add(@RequestBody @Validated SysCityAddDTO sysCityAddDTO) {
        int cityId;
        SysCity sysCity = sysCityAddDTO.toSysCity();
        sysCity.setGrade(1);
        if (sysCity.getParentId() > 0) {
            SysCity parentCity = sysCityService.getById(sysCity.getParentId());
            if (parentCity == null) {
                return Result.error("上级城市不存在，请检查");
            }
            cityId = sysCityService.generateCityId(parentCity);
            sysCity.setGrade(parentCity.getGrade() + 1);
        } else {
            cityId = sysCityService.generateCityId(null);
        }

        sysCity.setId(cityId);
        sysCity.setSpell(PinyinUtil.spellFull(sysCityAddDTO.getName()));

        sysCity.setDataStatus(DataStatus.NORMAL.getValue());
        sysCityService.save(sysCity);
        return Result.ok("添加省市区成功");
    }

    @ApiOperation(value = "省市区-更新")
    @PutMapping("/update")
    @OperateLog(description = "更新系统城市")
    public Result<String> update(@RequestBody @Validated SysCityUpdateDTO sysCityUpdateDTO) {
        SysCity oldSysCity = sysCityService.getById(sysCityUpdateDTO.getId());
        if (oldSysCity == null) {
            return Result.error("省市区不存在");
        }
        SysCity sysCity = sysCityUpdateDTO.toSysCity();
        sysCity.setGrade(1);
        if (sysCity.getParentId() > 0) {
            SysCity parentCity = sysCityService.getById(sysCity.getParentId());
            if (parentCity == null) {
                return Result.error("上级城市不存在，请检查");
            }
            sysCity.setGrade(parentCity.getGrade() + 1);
        }
        sysCity.setDataStatus(DataStatus.NORMAL.getValue());
        sysCity.setGrade(oldSysCity.getGrade());
        sysCityService.updateById(sysCity);
        return Result.ok("编辑省市区成功");
    }

    @ApiOperation(value = "省市区-删除")
    @DeleteMapping("/delete")
    @OperateLog(description = "删除系统城市")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("省市区不存在");
        }
        SysCity sysCity = sysCityService.getById(id);
        if (sysCity == null) {
            return Result.error("省市区不存在");
        }
        List<SysCity> childrenList = sysCityService.listByParentId(sysCity.getId());
        if (childrenList != null && childrenList.size() != 0) {
            return Result.error("不能级联删除,删除失败");
        }
        sysCityService.removeById(id);
        return Result.ok("删除省市区成功");
    }

    @ApiOperation(value = "树状列表")
    @GetMapping("/tree")
    public Result<List<SysCityVO>> tree(@RequestParam(defaultValue = "0", required = false) int grade) {
        List<SysCity> list = sysCityService.listByMaxGrade(grade);
        List<SysCityVO> cityTree = sysCityService.generateTree(list);
        return Result.data(cityTree);
    }

}
