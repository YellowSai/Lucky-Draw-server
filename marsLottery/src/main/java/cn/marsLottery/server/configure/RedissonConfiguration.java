package cn.marsLottery.server.configure;

import cn.marsLottery.server.config.AppConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfiguration {
    @Autowired
    private AppConfig appConfig;

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean("redissonClient")
    public RedissonClient redissonClient() throws IOException {
        String env = appConfig.getEnv();
        Config config = Config.fromYAML(RedissonConfiguration.class.getClassLoader().getResource("redisson-config-" + env + ".yml"));
        return Redisson.create(config);
    }
}
