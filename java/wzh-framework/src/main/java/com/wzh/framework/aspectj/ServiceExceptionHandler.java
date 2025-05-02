package com.wzh.framework.aspectj;

import com.wzh.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServiceExceptionHandler {

    @Around("@annotation(com.wzh.common.annotation.ServiceExceptionCatch)  || @within(com.wzh.common.annotation.ServiceExceptionCatch)")
    public Object serviceExceptionHandler(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            // 执行原始方法
            Object result = proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable e) {
            // 获取目标类和方法信息
            String className = proceedingJoinPoint.getTarget().getClass().getName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            // 记录异常日志
            log.error("Service层异常 拦截到 {}:{}.{} 方法异常", className, methodName, e.getMessage(), e);

            // 返回统一异常结果（根据项目实际封装）
            return AjaxResult.error(e.getMessage());

        }
    }
}
