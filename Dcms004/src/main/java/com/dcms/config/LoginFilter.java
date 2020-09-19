package com.dcms.config;

import com.dcms.pojo.login.LoginCount;
import com.dcms.pojo.login.LoginInfo;
import com.dcms.repository.LoginInfoRepository;
import com.dcms.service.LoginCountService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/14 0014 21:34
 * Description:登录过滤器
 **/
@WebFilter(urlPatterns = {"/user/toCusWel","/admin/toAdminWel","/doctor/toDocWel"})
public class LoginFilter implements Filter {
    @Resource
    private LoginCountService loginCountService;
    @Resource
    private LoginInfoRepository loginInfoRepository;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("-----------启动登录请求过滤器！准备获取今日游客，注册用户，就诊用户登录情况----------");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("开始过滤request信息");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Object username = session.getAttribute("userName");
        Object userId = session.getAttribute("userId");
        LoginInfo byId = loginInfoRepository.findById((String) userId);
        LoginCount count = new LoginCount();
        if(userId != null) {
            String sort = "";
            count.setUsername((String) username);
            count.setUserId((String) userId);
            count.setUserSort(byId.getUserSort());
            if(byId.getUserSort() == 1){
                sort = "客户";
            }else {
                sort = byId.getUserSort() == 2 ? "医生" : "管理员";
            }
            System.out.println(sort + " " + username + "登录，已记录！");
        }
        if(session.isNew() && userId == null){
            System.out.println("-----游客登录，已记录！----");
            count.setUsername("游客登录");
            count.setUserId("000");
            count.setUserSort(0);
        }
        loginCountService.save(count);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("-----------登录请求过滤器销毁！----------");
    }
}
