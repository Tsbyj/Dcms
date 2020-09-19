package com.dcms.handler;

import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.other.LayData;
import com.dcms.pojo.other.WorkLine;
import com.dcms.service.CustomerService;
import com.dcms.service.WorkLineService;
import org.springframework.stereotype.Controller;
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
 * Date: 2020/3/28 0028 21:45
 * Description:
 **/
@RequestMapping("/work")
@Controller
public class WorkLineHandler {
    @Resource
    private WorkLineService workLineService;
    @Resource
    private CustomerService customerService;

    @RequestMapping("/toAddWorkLine")
    public ModelAndView toAddWorkLine(){
        ModelAndView model = new ModelAndView();
        List<Customer> allCus = customerService.findAllCus();
        model.setViewName("doctor/customer/work_line");
        model.addObject("cus",allCus);
        return model;
    }
    @ResponseBody
    @RequestMapping("/insertWork")
    public Integer insertWork(@RequestBody WorkLine workLine, HttpSession session){
        String userId = (String) session.getAttribute("userId");
        workLine.setDocId(userId);
        return workLineService.saveNewWorkLine(workLine);
    }

    @ResponseBody
    @RequestMapping("/findAllNWork")
    public LayData findAllNWork(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        return workLineService.findNByDocId(userId);
    }
    @ResponseBody
    @RequestMapping("/updateFlag")
    public Integer updateFlag(@RequestBody WorkLine workLine){
//        System.out.println(workLine);
        return workLineService.updateFlag(workLine);
    }
}
