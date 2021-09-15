package cn.marsLottery.server.configure;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan(basePackages = {"cn.marsLottery.server.dao"})
public class DataSourceConfiguration {
}
