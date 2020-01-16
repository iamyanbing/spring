package com.iamyanbing.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author huangyanbing
 * @date 2019-10-14 16:28
 */
@Aspect
@Component
public class DataSourceAop {
/*    @Pointcut("!@annotation(com.iamyanbing.datasource.Master) " +
            "&& (execution(* com.createivearts.adminmanager.featuretwo.*.select*(..)) " +
            "|| execution(* com.createivearts.adminmanager.featuretwo.*.query*(..))" +
            "|| execution(* com.createivearts.adminmanager.featuretwo.*.count*(..))" +
            "|| execution(* com.createivearts.adminmanager.featuretwo.*.group*(..))" +
            "|| execution(* com.createivearts.adminmanager.featuretwo.*.get*(..)))")*/
    @Pointcut("!@annotation(com.iamyanbing.datasource.Master) " +
            "&& (execution(* com.iamyanbing.datasource.featuretwo.*.select*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featuretwo.*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.iamyanbing.datasource.Master) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.insert*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.add*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.update*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.edit*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.batch*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.delete*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featureone.*.remove*(..))")
    public void writePointcut() {

    }

    @Pointcut("!@annotation(com.iamyanbing.datasource.Master) " +
            "&& (execution(* com.iamyanbing.datasource.featurethree.*.insert*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featurethree.*.delete*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featurethree.*.select*(..)) " +
            "|| execution(* com.iamyanbing.datasource.featurethree.*.update*(..)))")
    public void featurethreePointcut() {

    }

    @Before("featurethreePointcut()")
    public void dwdUser() {
        DBContextHolder.dwdUser();
    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
//    @Before("execution(* com.cjs.example.service.impl.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }
}
