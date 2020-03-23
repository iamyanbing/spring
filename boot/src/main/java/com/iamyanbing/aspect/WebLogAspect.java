package com.iamyanbing.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 *  AOP的基本概念
 * (1)Aspect(切面):通常是一个类，里面可以定义切入点和通知
 * (2)JointPoint(连接点):程序执行过程中明确的点，一般是方法的调用
 * (3)Advice(通知):AOP在特定的切入点上执行的增强处理，有before,after,afterReturning,afterThrowing,around
 * (4)Pointcut(切入点):就是带有通知的连接点，在程序中主要体现为书写切入点表达式。切入点指切面具体织入的方法注意：被标记为final的方法是不能作为连接点与切入点的。因为最终的是不能被修改的，不能被增强的。
 * (5)AOP代理：AOP框架创建的对象，代理就是目标对象的加强。Spring中的AOP代理可以使JDK动态代理，也可以是CGLIB代理，前者基于接口，后者基于子类
 * (6)Weaving（织入):织入是指将切面代码插入到目标对象的过程。代理的invoke方法完成的工作，可以称为织入。
 *
 *  *  ：匹配任何数量字符；
 *  .. ：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
 *  +  ：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。
 *
 * A:@Pointcut("execution(* com.spring.service..*.*(..))")
 * execution:用来匹配执行方法的连接点
 * 第一个*表示匹配任意的方法返回值，..(两个点)表示零个或多个，上面的第一个..表示service包及其子包,第二个*表示所有类,第三个*表示所有方法，第二个..表示方法的任意参数个数
 *
 * B:@Pointcut("within(com.spring.service.*)")
 * within限定匹配方法的连接点,上面的就是表示匹配service包下的任意连接点
 *
 * 通过类匹配模式串声明切点，within()函数定义的连接点是针对目标类而言的，而非针对运行期对象的类型而言，这一点和execution()是相同的。
 * 但是within()和execution()函数不同的是，within()所指定的连接点最小范围只能是类，而execution()所指定的连接点可以大到包，小到方法入参。 所以从某种意义上讲，execution()函数功能涵盖了within()函数的功能
 * within(com.xgj.*) 匹配com.xgj包中的所有类的方法，但是不包含子孙包中类的方法
 * within(com.xgj..*)匹配com.xgj包以及子孙包中的所有类的方法都匹配这个切点
 *
 * C:@Pointcut("this(com.spring.service.UserService)")
 * this用来限定AOP代理必须是指定类型的实例，如上，指定了一个特定的实例，就是UserService
 *
 * D:@Pointcut("bean(userService)")
 * bean也是非常常用的,bean可以指定IOC容器中的bean的名称
 *
 *
 *  Around:环绕通知,在目标方法完成前后做增强处理,环绕通知是最重要的通知类型,像事务,日志等都是环绕通知,注意编程中核心是一个ProceedingJoinPoint
 * 执行实际的方法前： 先织入 around ，再织入 before；实际的方法执行完成后： 先织入 around ，再织入 after ，最后织入 afterreturning
 * 注意:Spring AOP的环绕通知会影响到AfterThrowing通知的运行。环绕通知处理了异常，AfterThrowing通知就不会执行；环绕通知没有处理异常，AfterThrowing通知才会执行。不要同时使用!同时使用也没啥意义。
 *
 * 对Web层做多个切面，校验用户，校验头信息等等，这个时候经常会碰到切面的处理顺序问题。
 * 设置切面的优先级，使用@Order(i)注解来标识切面的优先级。i的值越小，优先级越高
 * 在切入点前的操作，按order的值由小到大执行
 * 在切入点后的操作，按order的值由大到小执行
 *
 */
@Aspect
@Order(5)
@Component
public class WebLogAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 用com.iamyanbing.web.HelloController类进行测试
     */
    @Pointcut("execution(public * com.iamyanbing.web..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        LOGGER.info("URL : " + request.getRequestURL().toString());
        LOGGER.info("URI : " + request.getRequestURI());
        LOGGER.info("QueryString : " + request.getQueryString());//获取请求get地址后面带的参数信息
        LOGGER.info("HTTP_METHOD : " + request.getMethod());
        LOGGER.info("IP : " + request.getRemoteAddr());
        LOGGER.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        LOGGER.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    /**
     *  后置返回通知
     *  这里需要注意的是:
     *       如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     *       如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     *        returning：限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
     *        对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        LOGGER.info("RESPONSE : " + ret);
        LOGGER.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }
    /**
     * 后置最终通知（目标方法只要执行完了就会执行后置通知方法）
     * @param joinPoint
     */
    @After("webLog()")
    public void doAfterAdvice(JoinPoint joinPoint){
        LOGGER.info("后置最终通知执行了!!!!");
    }

    /**
     * 环绕通知：
     *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around("webLog()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        LOGGER.info("环绕通知的目标方法名："+proceedingJoinPoint.getSignature().getName());
        try {
            LOGGER.info("proceedingJoinPoint.proceed method before execution ");
            Object obj = proceedingJoinPoint.proceed();
            LOGGER.info("proceedingJoinPoint.proceed method after execution ");
            return obj;
        } catch (Throwable throwable) {
            LOGGER.info("proceedingJoinPoint.proceed method execution exception ; detail : " + throwable.getMessage());
        }
        return null;
    }

    /**
     * 后置异常通知
     *  定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     *  throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     *           对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "webLog()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){
        //目标方法名：
        LOGGER.info(joinPoint.getSignature().getName());
        if(exception instanceof NullPointerException){
            LOGGER.info("发生了空指针异常!!!!!");
        }else {
            LOGGER.info("发生了异常!!!!!");
        }
    }


}

