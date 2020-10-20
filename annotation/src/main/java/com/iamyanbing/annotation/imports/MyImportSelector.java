package com.iamyanbing.annotation.imports;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportSelector强调的是复用性
 *
 * 自定义逻辑返回需要导入的组件
 */
public class MyImportSelector implements ImportSelector {
    /**
     * @param importingClassMetadata  ： 当前标注@Import注解的类的所有注解信息
     * @return ： 返回值就是导入到IOC容器中的组件全类名
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] { "com.iamyanbing.annotation.imports.entry.Address",
                "com.iamyanbing.annotation.imports.entry.User"};
    }
}
