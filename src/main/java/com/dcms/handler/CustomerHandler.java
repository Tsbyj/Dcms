package com.dcms.handler;

import com.dcms.pojo.cus.Customer;
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

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/22 0022 17:16
 * Description:
 **/
@RequestMapping("/cus")
@Controller
public class CustomerHandler {
    @Resource
    private CustomerService customerService;
    @RequestMapping("/toCusList")
    public String toCusList(){
        return "doctor/customer/cus_list";
    }

    @RequestMapping("/toAddCus")
    public String toAddCus(){
        return "doctor/customer/cus_add";
    }

    // 展示客户信息列表
    @ResponseBody
    @RequestMapping("/findAllByDoc")
    public LayData getAll(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        ModelAndView model = new ModelAndView();
        LayData all = customerService.findAllByDoc(userId);
        model.setViewName("doctor/customer/cus_list");
        model.addObject("cusList", all);
        return all;
    }

    // 按ID搜索客户信息
    @ResponseBody
    @GetMapping("/findById")
    public LayData findById(String customerId, HttpSession session){
        String userId = (String) session.getAttribute("userId");
        LayData layData;
        if(customerId == null || customerId.equals("")){
            layData = customerService.findAllByDoc(userId);
        }else {
            layData = customerService.layFindById(customerId);
        }
        return layData;
    }

    // 按Name搜索客户信息
    @ResponseBody
    @GetMapping("/findByName")
    public LayData findByName(String customerName, HttpSession session){
        String userId = (String) session.getAttribute("userId");
        LayData layData;
        if(customerName == null || customerName.equals("")){
            layData = customerService.findAllByDoc(userId);
        }else {
            layData = customerService.layFindByName(customerName);
        }
        return layData;
    }

    // 新增客户信息
    @ResponseBody
    @RequestMapping("/insertCus")
    public Integer insertCus(@RequestBody Customer customer){
        System.out.println("insertCus中的Customer信息：" + customer);
        int index = customerService.save(customer);
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

    /*管理员模块*/
    @RequestMapping("/adminToList")
    public String adminToList(){
        return "admin/customer/cus_list";
    }

    @RequestMapping("/adminToAddCus")
    public String adminToAddCus(){
        return "admin/customer/cus_add";
    }
    // 展示客户信息列表 分页
    @ResponseBody
    @RequestMapping("/adminFindAll")
    public LayData adminFindAll(Integer page,Integer limit){
        return customerService.findAll(page,limit);
    }

    // 按ID搜索客户信息
    @ResponseBody
    @GetMapping("/adminFindById")
    public LayData adminFindById(String customerId){
        LayData layData;
        if(customerId == null || customerId.equals("")){
            layData = new LayData();
            layData.setCode(0);
        }else {
            layData = customerService.layFindById(customerId);
        }
        return layData;
    }

    // 按Name搜索客户信息
    @ResponseBody
    @GetMapping("/adminFindByName")
    public LayData adminFindByName(String customerName){
        LayData layData;
        if(customerName == null || customerName.equals("")){
            layData = new LayData();
            layData.setCode(0);
        }else {
            layData = customerService.layFindByName(customerName);
        }
        return layData;
    }
    // 按ID删除客户信息
    @ResponseBody
    @GetMapping("/deleteById")
    public Integer deleteById(String customerId){
        return customerService.deleteById(customerId);
    }
}
