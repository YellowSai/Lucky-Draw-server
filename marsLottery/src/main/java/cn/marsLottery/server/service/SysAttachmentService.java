package cn.marsLottery.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.SysAttachment;

import java.util.List;

/**
* 表[sys_attachment]对应的服务类
*
* @author auto
*/

public interface SysAttachmentService extends IService<SysAttachment> {
    SysAttachment getBySha1(String sha1);

    List<SysAttachment> listByIdIn(List<Integer> attachmentIdList);
}
