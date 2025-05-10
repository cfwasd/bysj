package com.wzh.front.filter;

/**
 * @author wzh
 * @Date 2025/4/19
 * @desc 文件码拦截器
 * @package com.wzh.front.interceptor
 */

import com.wzh.common.core.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义Filter
 * 对请求的header 过滤token
 *
 * 过滤器Filter可以拿到原始的HTTP请求和响应的信息，
 *     但是拿不到你真正处理请求方法的信息，也就是方法的信息
 *
 * @Component 注解让拦截器注入Bean，从而让拦截器生效
 * @WebFilter 配置拦截规则
 *
 * 拦截顺序：filter—>Interceptor-->ControllerAdvice-->@Aspect -->Controller
 *
 */
@Slf4j
@Component
@WebFilter(urlPatterns = {"/**"},filterName = "tokenAuthorFilter")
public class WebFilters implements Filter {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TokenFilter init {}",filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean isLock = redisCache.hasKey("error");
        if (isLock) {
            Integer num = redisCache.getCacheObject("error");
            if (num > 20){
                log.info("系统已停用");
                response.setContentType("application/json;charset=UTF-8");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(503);
                response.getWriter().write("{\"code\": 503, \"message\": \"系统已停用\"}");
            }else {
                chain.doFilter(request,response);//到下一个链
            }
        }else {
            chain.doFilter(request,response);
        }



    }

    @Override
    public void destroy() {
        log.info("TokenFilter destroy");
    }
}
