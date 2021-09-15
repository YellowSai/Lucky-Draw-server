package cn.marsLottery.server.controller.admapi;

import cn.marsLottery.server.dto.SysAttachmentAddDTO;
import cn.marsLottery.server.dto.SysAttachmentUpdateDTO;
import cn.marsLottery.server.po.SysAttachment;
import cn.marsLottery.server.service.SysAttachmentService;
import cn.marsLottery.server.vo.SysAttachmentVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import cn.jzcscw.commons.core.Result;
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

/**
* 表[sys_attachment]对应的后台管理类
*
* @author auto
*
*/

@Slf4j
@RestController
@RequestMapping("/admapi/sysAttachment")
@Api(tags = {"上传路径管理接口"})
public class SysAttachmentAdminController {

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @ApiOperation(value = "上传路径-列表")
    @GetMapping("/list")
    public Result<Page<SysAttachment>> list(Page page) {
        LambdaQueryWrapper<SysAttachment> queryWrapper = new LambdaQueryWrapper<>();
        //FIXME 根据需要增加搜索条件
        Page<SysAttachment> pageData = sysAttachmentService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "上传路径-详情")
    @GetMapping("/detail")
    public Result<SysAttachmentVO> detail(int id) {
        if (id <= 0) {
            return Result.error("上传路径不存在");
        }
        SysAttachment sysAttachment = sysAttachmentService.getById(id);
        if (sysAttachment == null) {
            return Result.error("上传路径不存在");
        }
        SysAttachmentVO sysAttachmentVO = new SysAttachmentVO(sysAttachment);
        return Result.data(sysAttachmentVO);
    }

    @ApiOperation(value = "上传路径-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated SysAttachmentAddDTO sysAttachmentAddDTO) {
        SysAttachment sysAttachment = sysAttachmentAddDTO.toSysAttachment();
        sysAttachmentService.save(sysAttachment);
        return Result.ok("添加上传路径成功");
    }

    @ApiOperation(value = "上传路径-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated SysAttachmentUpdateDTO sysAttachmentUpdateDTO) {
        SysAttachment oldSysAttachment = sysAttachmentService.getById(sysAttachmentUpdateDTO.getId());
        if (oldSysAttachment == null) {
            return Result.error("上传路径不存在");
        }
        SysAttachment sysAttachment = sysAttachmentUpdateDTO.toSysAttachment();
        BeanUtils.copyProperties(sysAttachment, oldSysAttachment);
        sysAttachmentService.updateById(sysAttachment);
        return Result.ok("编辑上传路径成功");
    }

    @ApiOperation(value = "上传路径-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("上传路径不存在");
        }
        SysAttachment sysAttachment = sysAttachmentService.getById(id);
        if (sysAttachment == null) {
            return Result.error("上传路径不存在");
        }
        sysAttachmentService.removeById(id);
        return Result.ok("删除上传路径成功");
    }
}
