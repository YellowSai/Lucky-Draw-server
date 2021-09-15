package cn.marsLottery.server.validate;

import cn.hutool.core.lang.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Validator.isMobile(value)) {
            return true;
        }
        return false;
    }
}
