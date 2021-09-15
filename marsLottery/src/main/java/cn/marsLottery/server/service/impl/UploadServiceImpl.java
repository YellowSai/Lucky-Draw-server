package cn.marsLottery.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.commons.util.StringUtil;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.po.SysAttachment;
import cn.marsLottery.server.service.SysAttachmentService;
import cn.marsLottery.server.service.UploadService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Override
    public String upload(MultipartFile file, String basePath, boolean jpgCompress) {
        try {
            String sha1 = SecureUtil.sha1(file.getInputStream());
            SysAttachment attachment = sysAttachmentService.getBySha1(sha1);
            if (attachment != null) {
                return attachment.getUrlPath();
            }

            String type = FileTypeUtil.getType(file.getInputStream());

            boolean isJPG = false;
            if (ImgUtil.IMAGE_TYPE_JPEG.equals(type) ||
                    ImgUtil.IMAGE_TYPE_JPG.equals(type)) {
                isJPG = true;
            }

            String filename = file.getOriginalFilename();
            String suffix = FileUtil.extName(filename);
            String filePrefix = UUID.randomUUID().toString();
            String newFileName = filePrefix + "." + suffix;
            String thumbFileName = filePrefix + "_thumb." + suffix;
            String relativeBasePath;
            if (StringUtil.isEmpty(basePath)) {
                relativeBasePath = "";
            } else {
                relativeBasePath = basePath + "/";
            }
            String datePrefix = DateUtil.format(DateUtil.date(), "yyyy-MM");
            relativeBasePath += "/" + datePrefix;

            File dir = FileUtil.file(appConfig.getUploadBasePath(), relativeBasePath);
            if (!dir.exists()) {
                log.debug("baseDir create:{}", dir.getAbsoluteFile());
                FileUtil.mkdir(relativeBasePath);
            }
            File outFile = FileUtil.file(appConfig.getUploadBasePath(), relativeBasePath + "/" + newFileName);

            log.debug("outFile:{}", outFile.getAbsoluteFile());

            FileUtil.writeFromStream(file.getInputStream(), outFile);
            try {
                if (jpgCompress && isJPG) {
                    ImgUtil.compress(outFile, outFile, 0.8f);
                }

                //生成一个缩略图
                if (ImgUtil.IMAGE_TYPE_JPEG.equals(type) || ImgUtil.IMAGE_TYPE_JPG.equals(type)
                        || ImgUtil.IMAGE_TYPE_BMP.equals(type) || ImgUtil.IMAGE_TYPE_GIF.equals(type)
                        || ImgUtil.IMAGE_TYPE_PNG.equals(type)) {
                    File thumbOutFile = FileUtil.file(appConfig.getUploadBasePath(), relativeBasePath + "/" + thumbFileName);
                    FileUtil.writeFromStream(file.getInputStream(), thumbOutFile);
                    ImgUtil.scale(thumbOutFile, thumbOutFile, 300, 300, null);
                }
            } catch (Exception e) {
                log.error("压缩图片/缩略图片出错=>{}", e);
            }
            String fileUrl = relativeBasePath + "/" + newFileName;
            attachment = new SysAttachment();
            attachment.setSha1(sha1);
            attachment.setPath(outFile.getAbsolutePath());
            attachment.setUrlPath(fileUrl);
            attachment.setCreateTime(DateUtil.date());
            attachment.setName(filename);
            sysAttachmentService.save(attachment);
            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传出错=>{}", e);
            throw new JzRuntimeException(e.getMessage());
        }
    }

    @Override
    public File getByUrl(String url) {
        if (url == null) {
            return null;
        }
        String path = url.replace(appConfig.getUploadBaseUrl(), appConfig.getUploadBasePath());
        File outFile = FileUtil.file(path);
        return outFile;
    }

    @Override
    public SysAttachment getSysAttachmentByUrl(String url) {
        return sysAttachmentService.getOne(new LambdaQueryWrapper<SysAttachment>().eq(SysAttachment::getUrlPath, url));
    }
}
