package ${config.basePackage}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ${config.basePackage}.dao.${model.model}Dao;
import ${config.basePackage}.po.${model.model};
import ${config.basePackage}.service.${model.model}Service;

/**
* ${model.model}对应的服务类实现
*
* @author ${config.author}
*/

@Service
public class ${model.model}ServiceImpl extends ServiceImpl<${model.model}Dao, ${model.model}> implements ${model.model}Service {

}
