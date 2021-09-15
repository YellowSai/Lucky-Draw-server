package cn.marsLottery.server.controller.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.marsLottery.server.dto.FakeLoginDTO;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.SmsService;
import cn.marsLottery.server.service.UserService;
import cn.marsLottery.server.util.SecretUtil;
import cn.marsLottery.server.vo.CustomerVO;
import cn.marsLottery.server.vo.LoginUserVO;
import cn.marsLottery.server.web.WebContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = {"前台登录接口"})
public class IndexController {
    public static String ApiKey = "vMBJjnkzH05lNvesgG3BNBSd3c9b5A";
    public static String actid = "410";
    public static String scope = "snsapi_userinfo";

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "测试登录", notes = "登陆成功则携带token返回")
    @PostMapping("/fake_login")
    @ApiAuth(ignore = true)
    public Result fake_login(@RequestBody @Validated FakeLoginDTO fakeLoginDTO) {

        User user = userService.getById(fakeLoginDTO.getUserId());

        //生成jwt
        String token = userService.createJWT(user);
        return Result.data(token);
    }

    @ApiOperation(value = "微信 - 微信账户和客户绑定", notes = "绑定接口")
    @PostMapping("/bindCustomer")
    @ApiAuth(ignore = true)
    public Result<CustomerVO> bindCustomer(@RequestParam(defaultValue = "0", required = false) String customerNumber,
                                           @RequestParam(defaultValue = "0", required = false) String mobile,
                                           @RequestParam(defaultValue = "0", required = false) String captcha) {
        try {
            smsService.checkCaptcha(mobile, captcha);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        Customer customer = customerService.getByCustomerNumberAndMobile(customerNumber,mobile);
        User user = userService.getById(WebContext.getUserId());
        if(user.getCustomerId() != 0){
            return Result.error("该微信已绑定过客户");
        }
        user.setCustomerId(customer.getId());
        userService.updateById(user);
        return Result.data(new CustomerVO(customer));
    }

    @ApiOperation(value = "验证码 - 获取登录/注册验证码")
    @GetMapping("/getRegCaptcha")
    @ApiAuth(ignore = true)
    public Result<String> getRegCaptcha(@RequestParam(defaultValue = "0", required = false) String mobile) {
        smsService.sendSmsCaptcha(mobile);
        return Result.ok("验证码已发送");
    }

    @ApiOperation(value = "微信 - 请求信息接口")
    @GetMapping("/authorize")
    @ApiAuth(ignore = true)
    public Result<Map<String, String>> authorize(String redirecturl) {
        String timestamp = String.valueOf(DateUtil.date().getTime());
        timestamp = timestamp.substring(0, timestamp.length() - 3);

        String MD5String = ApiKey
                + "actid=" + actid
                + "&redirecturl=" + redirecturl
                + "&scope=" + scope
                + "&timestamp=" + timestamp
                + "&userid=1";
        String versign;
        Map<String, String> map = new HashMap<>();
        try {
            versign = SecretUtil.MD5(MD5String);
            map.put("actid", actid);
            map.put("redirecturl", URLEncoder.encode(redirecturl, "utf8").toLowerCase());
            map.put("scope", scope);
            map.put("timestamp", timestamp);
            map.put("userid", "1");
            map.put("versign", versign);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.data(map);
    }

    @ApiOperation(value = "微信 - 微信登录接口", notes = "返回token用")
    @PostMapping("/login")
    @ApiAuth(ignore = true)
    public Result<LoginUserVO> login(String code) {
        log.debug(code);
        String timestamp = String.valueOf(DateUtil.date().getTime());
        timestamp = timestamp.substring(0, timestamp.length() - 3);
        String MD5String = ApiKey
                + "actid=" + actid
                + "&timestamp=" + timestamp
                + "&token=" + code
                + "&userid=1";
        String versign;
        String url = "https://open.esurl.cn/oauth2/userinfo";

        Map<String, String> jsonMap = new HashMap<>();
        try {
            versign = SecretUtil.MD5(MD5String);
            jsonMap.put("actid", actid);
            jsonMap.put("userid", "1");
            jsonMap.put("token", code);
            jsonMap.put("timestamp", timestamp);
            jsonMap.put("versign", versign);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        String json = HttpsUtil.doPostJson(url, jsonMap);
        String openId = new JSONObject(json).getStr("openid");
        if (openId == null) {
            return Result.error("openId请求异常");
        }
        User user = userService.getByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setData(json);
            userService.save(user);
            user = userService.getByOpenId(openId);
        }
        //生成jwt
        String token = userService.createJWT(user);

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(user.getId());
        loginUserVO.setCustomerId(user.getCustomerId());
        loginUserVO.setToken(token);

        return Result.data(loginUserVO);
    }
}
