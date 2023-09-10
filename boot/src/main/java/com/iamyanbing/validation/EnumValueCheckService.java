package com.iamyanbing.validation;

import com.google.common.collect.Sets;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : HuangYanBing
 * @date 2022/11/11 18:25
 */
public class EnumValueCheckService implements ConstraintValidator<EnumValueCheck, Object> {

    private Set<String> strs;
    private List<Integer> ints;

    @Override
    public void initialize(EnumValueCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        if (!ObjectUtils.isEmpty(constraintAnnotation.strs())) {
            strs = Sets.newHashSet(constraintAnnotation.strs());
        }
        if (!ObjectUtils.isEmpty(constraintAnnotation.ints())) {
            ints = Arrays.stream(constraintAnnotation.ints()).boxed().collect(Collectors.toList());
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return strs.contains(value);
        }
        if (value instanceof Integer) {
            return ints.contains(value);
//             ints.stream().anyMatch(e -> e.equals(value));
        }

        return false;
    }

    private boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
