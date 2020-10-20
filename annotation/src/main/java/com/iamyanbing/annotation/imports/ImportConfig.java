package com.iamyanbing.annotation.imports;

import com.iamyanbing.annotation.imports.entry.Address;
import com.iamyanbing.annotation.imports.entry.User;
import org.springframework.context.annotation.Import;

/**
 * 两种方式加载对象到IOC
 *
 * @Import({Address.class, User.class})  缺点：所有类都需要在这里先定义好
 *
 * @Import(MyImportSelector.class)  优点：程序运行时动态加载，参考spring boot
 */
//@Import({Address.class, User.class})
@Import(MyImportSelector.class)
public class ImportConfig {
}
