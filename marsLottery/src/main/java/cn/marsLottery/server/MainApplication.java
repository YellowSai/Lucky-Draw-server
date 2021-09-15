package cn.marsLottery.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot启动类
 */
@EnableScheduling
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class, scanBasePackages = {"cn.marsLottery.server", "cn.jzcscw"})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        springApplication.run(args);
    }

}
