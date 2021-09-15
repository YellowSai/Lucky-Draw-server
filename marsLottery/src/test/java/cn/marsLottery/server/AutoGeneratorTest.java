package cn.marsLottery.server;

import cn.jzcscw.generator.builder.AutoGenerator;
import cn.jzcscw.generator.config.BuilderConfig;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class AutoGeneratorTest {

    @Autowired
    private DynamicRoutingDataSource dataSource;


    @Test
    public void codeGenerator() {
        BuilderConfig config = new BuilderConfig();
        config.setAuthor("auto");
        config.setBasePackage("cn.marsLottery.server");
        config.setMapperDir("mybatis");

        AutoGenerator autoGenerator = new AutoGenerator(dataSource.getDataSource("mars_lottery"));

//        autoGenerator.generate(config, "lottery", "Lottery");
//        autoGenerator.generate(config, "customer", "Customer");
//        autoGenerator.generate(config, "lottery_customer", "LotteryCustomer");
//        autoGenerator.generate(config, "prize", "Prize");
//        autoGenerator.generate(config, "lottery_log", "LotteryLog");
//        autoGenerator.generate(config, "receipt", "Receipt");
//        autoGenerator.generate(config, "sys_attachment", "SysAttachment");
//        autoGenerator.generate(config, "user", "User");

    }
}
