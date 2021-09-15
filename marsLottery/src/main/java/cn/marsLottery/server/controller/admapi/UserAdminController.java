package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.UserAddDTO;
import cn.marsLottery.server.dto.UserUpdateDTO;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.UserService;
import cn.marsLottery.server.vo.UserVO;
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
 * 表[user]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/user")
@Api(tags = {"用户微信信息管理接口"})
public class UserAdminController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户微信信息-列表")
    @GetMapping("/list")
    public Result<Page<UserVO>> list(@RequestParam(defaultValue = "", required = false) String customerNumber,
                                     Page page) {

        Page<User> pageData = userService.pageBy(customerNumber, page);
        List<UserVO> userVOList = userService.toListVO(pageData.getRecords());

        Page<UserVO> userVOPage = new Page<>();
        userVOPage.setTotal(pageData.getTotal());
        userVOPage.setRecords(userVOList);
        userVOPage.setCurrent(page.getCurrent());
        userVOPage.setSize(page.getSize());

        return Result.data(userVOPage);
    }

    @ApiOperation(value = "用户微信信息-详情")
    @GetMapping("/detail")
    public Result<UserVO> detail(int id) {
        if (id <= 0) {
            return Result.error("用户微信信息不存在");
        }
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户微信信息不存在");
        }
        UserVO userVO = new UserVO(user);
        return Result.data(userVO);
    }

    @ApiOperation(value = "用户微信信息-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated UserAddDTO userAddDTO) {
        User user = userAddDTO.toUser();
        userService.save(user);
        return Result.ok("添加用户微信信息成功");
    }

    @ApiOperation(value = "用户微信信息-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        User oldUser = userService.getById(userUpdateDTO.getId());
        if (oldUser == null) {
            return Result.error("用户微信信息不存在");
        }
        User user = userUpdateDTO.toUser();
        BeanUtils.copyProperties(user, oldUser);
        userService.updateById(user);
        return Result.ok("编辑用户微信信息成功");
    }

}
