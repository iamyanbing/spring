package com.createivearts.adminmanager.datasource;

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
/*    @Pointcut("!@annotation(com.createivearts.adminmanager.datasource.Master) " +
            "&& (execution(* com.createivearts.adminmanager.dao.crm.*.select*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.query*(..))" +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.count*(..))" +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.group*(..))" +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.get*(..)))")*/
    @Pointcut("!@annotation(com.createivearts.adminmanager.datasource.Master) " +
            "&& (execution(* com.createivearts.adminmanager.dao.crm.CallTaskDao.selectAllByParamForJob(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.CallTaskDao.countByParamForJob(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.createivearts.adminmanager.datasource.Master) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.insert*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.add*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.update*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.edit*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.batch*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.delete*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.crm.*.remove*(..))")
    public void writePointcut() {

    }

    @Pointcut("!@annotation(com.createivearts.adminmanager.datasource.Master) " +
            "&& (execution(* com.createivearts.adminmanager.dao.dwduser.*.insert*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.dwduser.*.delete*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.dwduser.*.select*(..)) " +
            "|| execution(* com.createivearts.adminmanager.dao.dwduser.*.update*(..)))")
    public void dwdUserPointcut() {

    }

    @Before("dwdUserPointcut()")
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
