package com.dcms.handler;

import com.dcms.pojo.other.ConnectUs;
import com.dcms.pojo.other.LayData;
import com.dcms.pojo.news.News;
import com.dcms.pojo.cases.CaseInfos;
import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.doc.Doctor;
import com.dcms.service.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
 * Date: 2020/3/29 0029 11:59
 * Description:
 **/
@RequestMapping("/user")
@Controller
public class CusHandler {
    @Resource
    private DoctorService doctorService;
    @Resource
    private NewsService newsService;
    @Resource
    private CustomerService customerService;
    @Resource
    private WorkLineService workLineService;
    @Resource
    private CaseInfoService caseInfoService;
    @Resource
    private JavaMailSender javaMailSender;

    @RequestMapping("/toCusWel")
    public ModelAndView toCusWel(){
        ModelAndView model = new ModelAndView();
        List<Doctor> doctors = doctorService.showDoc();
        List<News> news = newsService.getAll();
        model.setViewName("user/cus_wel");
        model.addObject("docList",doctors);
        model.addObject("newsList",news);
        return model;
    }

    @RequestMapping("/toNewsList")
    public ModelAndView toNewsList(){
        ModelAndView model = new ModelAndView();
        model.setViewName("user/cus_newlist");
        List<News> allNews = newsService.getAllNews();
        model.addObject("newsList",allNews);
        return model;
    }

    @RequestMapping("/toNews")
    public ModelAndView toNews(String newsId){
        ModelAndView model = new ModelAndView();
        model.setViewName("user/cus_news");
        News byId = newsService.findById(newsId);
        List<News> other = newsService.findOther(newsId);
        model.addObject("news",byId);
        model.addObject("others",other);
        return model;
    }

    @RequestMapping("/toDocList")
    public ModelAndView toDocList(){
        ModelAndView model = new ModelAndView();
        List<Doctor> doctors = doctorService.showAllDoc();
        model.setViewName("user/cus_doclist");
        model.addObject("docList",doctors);
        return model;
    }

    @RequestMapping("/toDocInfo")
    public ModelAndView toDocInfo(String docId){
        ModelAndView model = new ModelAndView();
        Doctor byId = doctorService.findById(docId);
        model.addObject("doc",byId);
        model.setViewName("user/doc_info");
        return model;
    }

    @RequestMapping("/toCusInfo")
    public ModelAndView toCusInfo(HttpSession session){
        ModelAndView model = new ModelAndView();
        String cusId = (String) session.getAttribute("userId");
        if(cusId.contains("D") || cusId.equals("admin")){
            model.setViewName("user/login");
        }else {
            Customer byId = customerService.findById(cusId);
            model.addObject("cus",byId);
            model.setViewName("user/cus_info");
        }
        return model;
    }
    @ResponseBody
    @RequestMapping("/updateCus")
    public Integer updateCus(@RequestBody Customer customer){
        return customerService.update(customer);
    }
    
    @ResponseBody
    @RequestMapping("/registerCus")
    public Integer registerCus(@RequestBody Customer customer){
//        System.out.println(customer);
        int index = customerService.save(customer);
        return index;
    }

    @ResponseBody
    @RequestMapping("/findMyWork")
    public LayData findMyWork(HttpSession session){
        String cusId = (String) session.getAttribute("userId");
        LayData nByCusId = workLineService.findNByCusId(cusId);
        return nByCusId;
    }

    @ResponseBody
    @RequestMapping("/findAllCase")
    public LayData findAllCase(HttpSession session){
        String cusId = (String) session.getAttribute("userId");
        LayData byCusIdR = caseInfoService.findByCusIdR(cusId);
        return byCusIdR;
    }

    @RequestMapping("/findOneAll")
    public ModelAndView findOneAllMess(String caseId) {
        List<CaseInfos> reVisit;
        ModelAndView model = new ModelAndView();
        // 判断该病例有复诊信息，>1说明有复诊信息
        Integer idNum = caseInfoService.getIdNum(caseId);
        CaseInfos caseInfos = caseInfoService.findOneAllMess(caseId);
        if(idNum > 1){
            reVisit = caseInfoService.findReVisit(caseId);
            model.addObject("reVisit", reVisit);
        }
        // 借用doctor中的病例详情界面展示信息
        model.setViewName("doctor/caseInfo/case_info");
        model.addObject("caseInfos", caseInfos);
        return model;
    }

    @ResponseBody
    @RequestMapping("/connectUs")
    public String connectUs(@RequestBody ConnectUs connectUs){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("18334758861@163.com");
        message.setTo("2240423848@qq.com");
        message.setSubject("私人牙医游客来访信息");
        message.setText(connectUs.getContent() + "\n"
                + "联系电话：" + connectUs.getPhone() + "\n"
                + "联系地址：" + connectUs.getAddress() + "\n"
                + "联系邮箱：" + connectUs.getEmail() + "\n"
                + "游客姓名：" + connectUs.getName());
        System.out.println(message.getText());
        try {
            javaMailSender.send(message);
            System.out.println("发送成功！");
        }catch (Exception e){
            System.out.println(e);
        }
        return "感谢您的联系！";
    }
}
