package cn.marsLottery.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.jzcscw.commons.util.StringUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.marsLottery.server.dao.SysCityDao;
import cn.marsLottery.server.enums.ChinaSpecialDistrict;
import cn.marsLottery.server.po.SysCity;
import cn.marsLottery.server.service.SysCityService;
import cn.marsLottery.server.vo.CityVO;
import cn.marsLottery.server.vo.SysCityVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * SysCity对应的服务类实现
 *
 * @author auto
 */

@Service
public class SysCityServiceImpl extends ServiceImpl<SysCityDao, SysCity> implements SysCityService {

    private static final int initSeed = 10;

    /**
     * 最大级别
     */
    private static final int maxGrade = 4;

    private static final int chinaId = 10000000;

    private static final String cityVoCaCheKey = "cityVO";

    @Autowired
    private CacheManager cacheManager;

    @Override
    public List<SysCity> listByMaxGrade(int maxGrade) {
        LambdaQueryWrapper<SysCity> queryWrapper = new LambdaQueryWrapper<>();
        if (maxGrade > 0) {
            queryWrapper.le(SysCity::getGrade, maxGrade);
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<SysCityVO> generateTree(List<SysCity> list) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }

        Map<Long, List<SysCityVO>> cityListMap = new HashMap<>();
        for (SysCity sysCity : list) {
            cityListMap.computeIfAbsent(sysCity.getParentId(), a -> new ArrayList<>()).add(new SysCityVO(sysCity));
        }
        List<SysCityVO> tree = cityListMap.get(0L);
        tree.forEach(c -> setChildren(c, cityListMap));
        return tree;
    }

    @Override
    public List<SysCity> listByParentId(long id) {
        return this.list(new LambdaQueryWrapper<SysCity>().eq(SysCity::getParentId, id));
    }

    @Override
    public long getMaxCityId(long parentId) {
        LambdaQueryWrapper<SysCity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCity::getParentId, parentId).orderByDesc(SysCity::getId).last("limit 1");
        SysCity sysCity = this.getOne(queryWrapper);
        if (sysCity == null) {
            return 0;
        }
        return sysCity.getId();
    }

    @Override
    public int generateCityId(SysCity parentCity) {
        //按照规则生成城市id
        // grade 1 10 * 100^0 = 10 * 100^3 = 10000000
        // grade 2 10 * 100^1 = 1000 * 100^2 = 10000000
        // grade 3 10 * 100^2 = 100000 * 100^1 = 10000000
        // grade 4 10 * 100^3 = 10000000 *100^0 = 10000000
        int grade = 1;
        if (parentCity != null) {
            grade = parentCity.getGrade() + 1;
        }
        long maxId = getMaxCityId(parentCity == null ? 0 : parentCity.getId());
        if (maxId == 0) {
            if (grade > 1) {
                maxId = parentCity.getId();
            } else {
                return (int) (initSeed * Math.pow(100, maxGrade - 1));
            }
        }
        int currentId = (int) (maxId / Math.pow(100, maxGrade - grade));
        currentId = currentId + 1;
        return (int) (currentId * Math.pow(100, maxGrade - grade));
    }

    @Override
    public List<SysCity> listByParentIdIn(List<Integer> cityIdList) {
        if (CollectionUtil.isEmpty(cityIdList)) {
            return new ArrayList<>();
        }
        return this.list(new LambdaQueryWrapper<SysCity>().in(SysCity::getParentId, cityIdList));
    }

    @Override
    public SysCity getByAddress(String address) {
        String city = this.resolutionCityName(address);

        SysCity sysCity = null;
        if (StringUtil.isNotEmpty(city)) {
            sysCity = this.getLikeNameAndGrade(city, 3);
        }
        return sysCity;
    }

    @Override
    public SysCity getLikeNameAndGrade(String address, int grade) {
        return this.getOne(new LambdaQueryWrapper<SysCity>()
                .like(SysCity::getName, address)
                .eq(SysCity::getGrade, grade)
                .last("limit 1")
        );
    }

    @Override
    public List<SysCity> listByIdIn(List<Integer> cityIdList) {
        if (CollectionUtil.isEmpty(cityIdList)) {
            return new ArrayList<>();
        }
        return this.listByIds(cityIdList);
    }

    @Override
    public CityVO getCityVo(String address) {
        CityVO cityVO = (CityVO) cacheManager.get(cityVoCaCheKey);
        if (cityVO == null) {
            cityVO = this.refreshCityVo();
        }

        // 定位城市
        SysCityVO city = this.getCurrentCity(address);
        cityVO.setCity(city);

        return cityVO;
    }

    @Override
    public CityVO refreshCityVo() {
        List<SysCity> list = this.listByMaxGrade(0);
        list.sort(Comparator.comparingInt(SysCity::getGrade));
        List<SysCityVO> voList = list.stream().map(SysCityVO::new).collect(Collectors.toList());
        Map<Long, SysCityVO> sysCityMap = voList.stream().collect(Collectors.toMap(SysCityVO::getId, c -> c));

        Map<Long, List<SysCityVO>> cityListMap = new HashMap<>();
        for (SysCityVO sysCityVO : voList) {
            cityListMap.computeIfAbsent(sysCityVO.getParentId(), a -> new ArrayList<>()).add(sysCityVO);
        }

        CityVO cityVO = new CityVO();
        // 国内城市
        SysCityVO china = this.getChinaCity(voList, sysCityMap, cityListMap);
        cityVO.setChina(china);

        // 港澳台
        SysCityVO specialDistrict = this.getSpecialDistrict(sysCityMap, cityListMap);
        cityVO.setSpecialDistrict(specialDistrict);

        // 国外城市
        SysCityVO foreignCity = this.getForeignCity(voList, cityListMap);
        cityVO.setForeignCity(foreignCity);

        cacheManager.set(cityVoCaCheKey, cityVO);
        return cityVO;
    }

    private SysCityVO getForeignCity(List<SysCityVO> voList, Map<Long, List<SysCityVO>> cityListMap) {
        List<SysCityVO> sysCityVOList = new ArrayList<>();
        for (SysCityVO sysCityVO : voList) {
            if (sysCityVO.getParentId() == 0 && sysCityVO.getId() != chinaId) {
                this.setChildren(sysCityVO, cityListMap);
                sysCityVOList.add(sysCityVO);
            }
        }

        SysCityVO foreignCity = new SysCityVO();
        foreignCity.setName("国外城市");
        foreignCity.setChildren(sysCityVOList);

        return foreignCity;
    }


    private SysCityVO getCurrentCity(String address) {
        if (StringUtil.isEmpty(address)) {
            return new SysCityVO();
        }
        SysCity sysCity = this.getByAddress(address);
        if (sysCity == null) {
            return null;
        }

        List<SysCity> childrenList = this.listByParentId(sysCity.getId());
        List<SysCityVO> voList = childrenList.stream().map(SysCityVO::new).collect(Collectors.toList());

        Map<Long, List<SysCityVO>> cityListMap = new HashMap<>();
        for (SysCityVO sysCityVO : voList) {
            cityListMap.computeIfAbsent(sysCityVO.getParentId(), a -> new ArrayList<>()).add(sysCityVO);
        }

        SysCityVO city = new SysCityVO(sysCity);
        this.setChildren(city, cityListMap);

        List<SysCityVO> childrenVOList = new ArrayList<>();
        childrenVOList.add(city);

        SysCityVO currentCity = new SysCityVO();
        currentCity.setName("当前城市");
        currentCity.setChildren(childrenVOList);
        return currentCity;
    }

    private SysCityVO getChinaCity(List<SysCityVO> voList, Map<Long, SysCityVO> sysCityMap, Map<Long, List<SysCityVO>> cityListMap) {
        List<SysCityVO> chinaCity = new ArrayList<>();

        SysCityVO china = sysCityMap.get(chinaId);
        if (china == null) {
            return null;
        }

        for (SysCityVO sysCityVO : voList) {
            if (sysCityVO.getParentId() == chinaId) {
                // 将港澳台去除，放到其他特殊分类中
                if (ChinaSpecialDistrict.getIdList().contains(sysCityVO.getId())) {
                    continue;
                }
                this.setChildren(sysCityVO, cityListMap);
                chinaCity.add(sysCityVO);
            }
        }
        china.setChildren(chinaCity);
        return china;
    }

    private SysCityVO getSpecialDistrict(Map<Long, SysCityVO> sysCityMap, Map<Long, List<SysCityVO>> cityListMap) {
        List<Integer> specialDistrictIdList = ChinaSpecialDistrict.getIdList();

        List<SysCityVO> result = new ArrayList<>();
        for (Integer id : specialDistrictIdList) {
            SysCityVO sysCityVO = sysCityMap.get(id);
            this.setChildren(sysCityVO, cityListMap);
            result.add(sysCityVO);
        }

        SysCityVO specialDistrict = new SysCityVO();
        specialDistrict.setName("港澳台");
        specialDistrict.setChildren(result);
        return specialDistrict;
    }

    private SysCityVO setChildren(SysCityVO parent, Map<Long, List<SysCityVO>> cityListMap) {
        List<SysCityVO> cityList = cityListMap.get(parent.getId());
        if (CollectionUtil.isEmpty(cityList)) {
            return parent;
        }

        for (SysCityVO sysCityVO : cityList) {
            this.setChildren(sysCityVO, cityListMap);
        }
        parent.setChildren(cityList);
        return parent;
    }

    private String resolutionCityName(String address) {
        String regex = "[^(省|上海|北京|天津|重庆|自治区)]+(市|县|州|自治州|区)";
        Matcher matcher = Pattern.compile(regex).matcher(address);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return address;
    }

}
