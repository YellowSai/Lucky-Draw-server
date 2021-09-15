package cn.marsLottery.server.vo;

import cn.jzcscw.commons.constant.YesNoStatus;
import cn.marsLottery.server.po.SysPermission;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class SysPermissionVO {

    private int id;

    private int parentId;

    private int type;

    private String component;

    private String title;

    private String name;

    private String key;

    private String path;

    private String redirect;

    private int sort;

    private String permits;

    private boolean hidden;

    private Meta meta;

    private List<SysPermissionVO> children;

    public SysPermissionVO(SysPermission permission) {
        if (permission != null) {
            BeanUtils.copyProperties(permission, this);
            this.hidden = YesNoStatus.isYes(permission.getHidden());
            this.name = permission.getKey();
            this.meta = new Meta();
            this.meta.setTitle(permission.getTitle());
            this.meta.setIcon(permission.getIcon());
        }
    }

    @Data
    private static class Meta {
        private String icon;
        private String title;
        private String url;
        private String target;
    }
}
