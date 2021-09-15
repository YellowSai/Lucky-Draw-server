package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.SysCity;
import lombok.Data;

import java.util.List;

/**
 * 表[sys_city]对应VO类
 *
 * @author auto
 */

@Data
public class SysCityVO {

    private long id;

    private long parentId;

    private String name;

    private int grade;

    private int weight;

    private List<SysCityVO> children;

    public SysCityVO(SysCity sysCity) {
        if (sysCity != null) {
            BeanUtil.copyProperties(sysCity, this);
        }
    }

    public SysCityVO() {
    }
}
