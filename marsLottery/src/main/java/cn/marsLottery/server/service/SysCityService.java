package cn.marsLottery.server.service;

import cn.marsLottery.server.po.SysCity;
import cn.marsLottery.server.vo.CityVO;
import cn.marsLottery.server.vo.SysCityVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 表[sys_city]对应的服务类
 *
 * @author auto
 */

public interface SysCityService extends IService<SysCity> {

    List<SysCity> listByParentId(long id);

    List<SysCity> listByMaxGrade(int maxGrade);

    List<SysCityVO> generateTree(List<SysCity> list);

    long getMaxCityId(long parentId);

    int generateCityId(SysCity parentCity);

    List<SysCity> listByParentIdIn(List<Integer> cityIdList);

    SysCity getByAddress(String address);

    SysCity getLikeNameAndGrade(String address, int grade);

    List<SysCity> listByIdIn(List<Integer> cityIdList);

    CityVO getCityVo(String address);

    CityVO refreshCityVo();
}
