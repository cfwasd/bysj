package com.wzh.front.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wzh
 * @Date 2025/4/19
 * @desc 文件码拦截器
 * @package com.wzh.front.interceptor
 */

public class FileControllerInter implements HandlerInterceptor {

    @Resource
    public RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //待实现校验文件码
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
