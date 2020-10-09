package com.jay.blog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * 记录用户的访问
 * */

public class VisitedInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(VisitedInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder parameters = new StringBuilder();
        for (String par : parameterMap.keySet()){
            String[] values = parameterMap.get(par);
            parameters.append(par).append("=");
            for (String value : values)
                parameters.append(value).append(" ");
            parameters.append(",");
        }
        String url = request.getRequestURL().toString();
        String parameterStr = parameters.toString();
        if (parameterStr.length() > 50)
            parameterStr = parameterStr.substring(50);
        logger.info("ip:{}, url:{}, parameters:{}", ip,url, parameterStr);

        return true;
    }
}
