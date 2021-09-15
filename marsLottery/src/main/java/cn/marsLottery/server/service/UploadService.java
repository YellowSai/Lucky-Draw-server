package cn.marsLottery.server.service;


import cn.marsLottery.server.po.SysAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface UploadService {

    String upload(MultipartFile file, String basePath, boolean jpgCompress);
    File getByUrl(String url);
    SysAttachment getSysAttachmentByUrl(String url);
}
