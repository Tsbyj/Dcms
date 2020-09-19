package com.dcms.handler;

import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.other.CustomerAreaData;
import com.dcms.pojo.other.CustomerLoginData;
import com.dcms.pojo.other.CustomerSexData;
import com.dcms.pojo.other.LayData;
import com.dcms.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/4/18 0018 20:20
 * Description:
 **/
@Controller
@RequestMapping("/admin")
public class AdminHandler {
    @Resource
    private CustomerService customerService;

    @RequestMapping("/toCusList")
    public String toList(){
        return "admin/customer/cus_list";
    }

    @RequestMapping("/toCaseList")
    public String toCaseList(){
        return "admin/doctor/case_list";
    }

    @RequestMapping("/toAdminRePass")
    public String toRePass(){
        return "admin/admin_mess";
    }

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "admin/customer/cus_index";
    }

    @RequestMapping("/toAdminWel")
    public String toWel(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if(userId.equals("admin")){
            return "admin/admin_wel";
        }else {
            return "redirect:/login/toDocLogin";
        }
    }

    @RequestMapping("/toAddCus")
    public String toAddCus(){
        return "admin/customer/cus_add";
    }

    // 按ID搜索客户信息
    @ResponseBody
    @GetMapping("/findById")
    public LayData findById(String customerId){
        LayData layData = customerService.layFindById(customerId);
        return layData;
    }

    // 按Name搜索客户信息
    @ResponseBody
    @GetMapping("/findByName")
    public LayData findByName(String customerName){
        LayData layData = customerService.layFindByName(customerName);
        return layData;
    }

    // 按ID删除客户信息
    @ResponseBody
    @GetMapping("/deleteById")
    public Integer deleteById(String customerId){
        Integer index = customerService.deleteById(customerId);
        return index;
    }

    //    执行弹出窗的操作
    @RequestMapping("/toCusInfo")
    public ModelAndView toCusInfo(String customerId){
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/customer/cus_info");
        Customer customer = customerService.findById(customerId);
        model.addObject("cus",customer);
        return model;
    }
    // 修改客户信息
    @ResponseBody
    @RequestMapping("/saveCus")
    public Integer saveCus(@RequestBody Customer customer){
        System.out.println("获取到的Customer信息：" + customer);
        int index = customerService.update(customer);
        return index;
    }
    // 新增客户信息
    @ResponseBody
    @RequestMapping("/insertCus")
    public Integer insertCus(@RequestBody Customer customer){
        System.out.println("insertCus中的Customer信息：" + customer);
        int index = customerService.save(customer);
        return index;
    }
    @ResponseBody
    @RequestMapping("/toCusIndex")
    private CustomerAreaData toCusIndex(){
        CustomerAreaData cusAreaData = customerService.findCusAreaData();
        return cusAreaData;
    }
    //获取客户年龄段接口
    @ResponseBody
    @RequestMapping("/toCusIndex1")
    private CustomerLoginData toCusIndex1(){
        CustomerLoginData cusLoginMes = customerService.findCusLoginMes();
        return cusLoginMes;
    }
    @ResponseBody
    @RequestMapping("/toCusIndex2")
    private List<CustomerSexData> toCusIndex2(){
        List<CustomerSexData> cusSexData = customerService.findCusSexData();
        return cusSexData;
    }
}
