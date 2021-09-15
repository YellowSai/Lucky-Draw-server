package cn.marsLottery.server.service.impl;

import cn.marsLottery.server.dao.SysLogDao;
import cn.marsLottery.server.po.SysLog;
import cn.marsLottery.server.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* SysLog对应的服务类实现
*
* @author auto
*/

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

}
