package com.wzh.common.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE}) //可在类或者方法使用
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceExceptionCatch {
}
