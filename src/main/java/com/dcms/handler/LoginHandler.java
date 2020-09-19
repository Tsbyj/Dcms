package com.dcms.handler;

import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.login.LoginInfo;
import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.other.CustomerWeekData;
import com.dcms.repository.LoginInfoRepository;
import com.dcms.service.CustomerService;
import com.dcms.service.DoctorService;
import com.dcms.service.LoginCountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/20 0020 18:20
 * Description:
 **/
@RequestMapping("/login")
@Controller
public class LoginHandler {
    @Resource
    private LoginInfoRepository loginInfoRepository;
    @Resource
    private DoctorService doctorService;
    @Resource
    private CustomerService customerService;
    @Resource
    private LoginCountService loginCountService;

    // ----------------------------客户登录模块---
    @RequestMapping("/toUserLogin")
    public String toUserLogin(HttpSession session){
        String cusName = (String) session.getAttribute("userName");
        String cusId = (String) session.getAttribute("userId");
        Customer byId = customerService.findById(cusId);
        if(cusName != null && cusId != null && byId != null){
            return "redirect:/user/toCusInfo";
        }else {
            return "user/login";
        }
    }

    @RequestMapping("/toUserRegister")
    public String toUserRegister(){
        return "user/cus_register";
    }

    @RequestMapping("/userLogin")
    public ModelAndView userLogin(LoginInfo loginInfo, HttpServletRequest req){
        ModelAndView model = new ModelAndView();
        HttpSession session = req.getSession();
        LoginInfo byName = loginInfoRepository.findByName(loginInfo.getUsername());
        if (byName == null){
            model.setViewName("forward:toUserLogin");
            model.addObject("message","该用户未注册！");
        }else if(byName.getUserSort() != 1){
            model.setViewName("forward:toUserLogin");
            model.addObject("message","该账号非客户！");
        } else if(loginInfo.getUsername().equals(byName.getUsername())
                && loginInfo.getPassword().equals(byName.getPassword())
                && byName.getUserSort() == 1){
            model.setViewName("redirect:/user/toCusInfo");
            session.setAttribute("userName",byName.getUsername());
            session.setAttribute("userId",byName.getUserId());
        }else {
            model.setViewName("forward:toUserLogin");
            model.addObject("message","用户名或密码错误！");
        }
        return model;
    }
    // ----------------------------医生登录模块-----
    @RequestMapping("/toDocLogin")
    public String toDocLogin(){
        return "doctor/login";
    }

    //  后台验证登录
    @RequestMapping("/docLogin")
    public ModelAndView docLogin(LoginInfo loginInfo, HttpServletRequest request){
        ModelAndView model = new ModelAndView();
        HttpSession session = request.getSession();
        LoginInfo byName = loginInfoRepository.findByName(loginInfo.getUsername());
        if (byName == null){
            model.setViewName("forward:toDocLogin");
            model.addObject("message","该用户未注册！");
        }else if(loginInfo.getUsername().equals(byName.getUsername())
                && loginInfo.getPassword().equals(byName.getPassword())
                && byName.getUserSort() != 1){
            if(byName.getUserSort() == 3){
                model.setViewName("redirect:/admin/toAdminWel");
            }else if(byName.getUserSort() == 2){
                model.setViewName("redirect:/doctor/toDocWel");
            }
            session.setAttribute("userName",byName.getUsername());
            session.setAttribute("userId",byName.getUserId());
        }else {
            model.setViewName("forward:toDocLogin");
            model.addObject("message","用户名或密码错误！");
        }
        return model;
    }

    // 修改密码
    @ResponseBody
    @RequestMapping("/updateDocMess")
    public Integer updateMess(@RequestBody LoginInfo loginInfo, HttpSession session){
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        LoginInfo byId = loginInfoRepository.findById(userId);
        loginInfo.setUserId(userId);
        loginInfo.setUsername(userName);
        loginInfo.setUserSort(byId.getUserSort());
        return loginInfoRepository.update(loginInfo);
    }

    /*管理员模块*/
    @RequestMapping("/toAdminRePass")
    public String toRePass(){
        return "admin/admin_mess";
    }

    @ResponseBody
    @RequestMapping("/findWeekData")
    public List<CustomerWeekData> findWeekData(){
        return loginCountService.getWeekData();
    }
    // 通用退出
    @RequestMapping("/edit")
    public String edit(HttpSession session){
        Enumeration<String> em = session.getAttributeNames();
        // 清除所有session数据，准备退出
        while (em.hasMoreElements()) {
            session.removeAttribute(em.nextElement());
        }
        System.out.println("------------用户已注销-------------");
        return "redirect:/login/toDocLogin";
    }
    // 通用获取密码
    @ResponseBody
    @RequestMapping("/getPassword")
    public String getPassword(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        LoginInfo byId = loginInfoRepository.findById(userId);
        return byId.getPassword();
    }
}
