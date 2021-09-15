package cn.marsLottery.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.marsLottery.server.dao.SysAttachmentDao;
import cn.marsLottery.server.po.SysAttachment;
import cn.marsLottery.server.service.SysAttachmentService;

import java.util.ArrayList;
import java.util.List;

/**
* SysAttachment对应的服务类实现
*
* @author auto
*/

@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentDao, SysAttachment> implements SysAttachmentService {

    @Override
    public SysAttachment getBySha1(String sha1) {
        LambdaQueryWrapper<SysAttachment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAttachment::getSha1, sha1);
        return getOne(queryWrapper);
    }

    @Override
    public List<SysAttachment> listByIdIn(List<Integer> attachmentIdList) {
        if (CollectionUtil.isEmpty(attachmentIdList)) {
            return new ArrayList<>();
        }
        return this.listByIds(attachmentIdList);
    }

}
