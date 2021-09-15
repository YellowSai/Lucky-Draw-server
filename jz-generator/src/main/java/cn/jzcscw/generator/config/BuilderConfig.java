package cn.jzcscw.generator.config;

import lombok.Data;

import java.util.Date;

@Data
public class BuilderConfig {
    /**
     * 包名
     */
    private String basePackage = "";
    /**
     * 作者
     */
    private String author = "auto";

    /**
     * mapper文件路径
     */
    private String mapperDir = "";
}
