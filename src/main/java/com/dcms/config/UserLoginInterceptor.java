package com.dcms.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/21 0021 11:15
 * Description: 设置自定义拦截器
 **/
@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        Object username = session.getAttribute("userName");
        String userId = (String) session.getAttribute("userId");
        if(username != null && userId != null){
            return true;
        }else {
            response.sendRedirect(request.getContextPath()+"/login/toDocLogin");
            return false;
        }
    }
}
