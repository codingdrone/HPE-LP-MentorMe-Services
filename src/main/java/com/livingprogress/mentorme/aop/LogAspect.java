package com.livingprogress.mentorme.aop;

import com.livingprogress.mentorme.utils.Helper;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * The aop log aspect.
 */
@Aspect
@Component
public class LogAspect {
    /**
     * The logger pointcut.
     */
    private static final String LOG_POINTCUT = "execution(* com.livingprogress.mentorme.services.*.*(..)) || " +
            "execution(* com.livingprogress.mentorme.controllers.*.*(..)) || execution(* com.livingprogress.mentorme" +
            ".security.SimpleUserDetailsService.*(..))";

    /**
     * The logger with package name.
     */
    public static final Logger LOGGER = Logger.getLogger("com.livingprogress.mentorme");

    /**
     * Log method entrance.
     *
     * @param joinPoint the joint point
     */
    @Before(LOG_POINTCUT)
    public void logMethodEntrance(JoinPoint joinPoint) {
        String[] parameterNames = ((MethodSignature) (joinPoint.getSignature())).getParameterNames();
        if (parameterNames == null) {
            parameterNames = new String[joinPoint.getArgs().length];
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                parameterNames[i] = "arg" + i;
            }
        }
        Helper.logEntrance(LOGGER, joinPoint.getSignature()
                                            .toString(), parameterNames, joinPoint.getArgs());
    }

    /**
     * Log method exit.
     *
     * @param joinPoint the join point
     * @param result    the result
     */
    @AfterReturning(pointcut = LOG_POINTCUT, returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        Helper.logExit(LOGGER, joinPoint.getSignature()
                                        .toString(), result);
    }

    /**
     * Log exception.
     *
     * @param joinPoint the joint point
     * @param ex        the exception
     */
    @AfterThrowing(pointcut = LOG_POINTCUT, throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        Helper.logException(LOGGER, joinPoint.getSignature()
                                             .toString(), ex);
    }
}
