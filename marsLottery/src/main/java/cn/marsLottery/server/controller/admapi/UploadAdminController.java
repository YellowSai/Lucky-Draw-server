package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/admapi/upload")
public class UploadAdminController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    public Result<HashMap<String, Object>> uploadFile(@RequestParam @NotNull(message = "上传文件不能为空") MultipartFile file) {
        String fileUrl = uploadService.upload(file, null, true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("url", fileUrl);
        return Result.data(map);
    }
}

