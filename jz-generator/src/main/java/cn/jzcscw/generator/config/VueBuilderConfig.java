package cn.jzcscw.generator.config;

import lombok.Data;

@Data
public class VueBuilderConfig {
    /**
     * 包名
     */
    private String projectDir = "";
    /**
     * 作者
     */
    private String author = "auto";
    /**
     * 是否显示增加按钮
     */
    private boolean operateAdd = true;
    /**
     * 是否显示删除按钮
     */
    private boolean operateDelete = true;
    /**
     * 是否显示编辑按钮
     */
    private boolean operateEdit = true;
    /**
     * 是否显示导出按钮
     */
    private boolean operateExport = false;
    /**
     * 是否显示导入按钮
     */
    private boolean operateImport = false;
}
