package com.iamyanbing.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author : HuangYanBing
 * @date 2022/11/11 18:19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = EnumValueCheckService.class)
public @interface EnumValueCheck {

    /**
     * 字符串数组
     */
    String[] strs() default {};

    /**
     * int数组
     */
    int[] ints() default {};

    String message() default "请求参数不符合规范";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
