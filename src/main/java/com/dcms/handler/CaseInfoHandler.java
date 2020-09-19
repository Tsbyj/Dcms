package com.dcms.handler;

import com.dcms.pojo.cases.CaseInfo;
import com.dcms.pojo.cases.CaseInfos;
import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.other.LayData;
import com.dcms.service.CaseInfoService;
import com.dcms.service.CustomerService;
import com.dcms.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/21 0021 14:51
 * Description:
 **/
@RequestMapping("/case")
@Controller
public class CaseInfoHandler {
    @Resource
    private CaseInfoService caseInfoService;
    @Resource
    private CustomerService customerService;
    @Resource
    private DoctorService doctorService;

    @RequestMapping("/toList1")
    public String toList1() {
        return "doctor/caseInfo/case_list1";
    }
    @RequestMapping("/toList2")
    public String toList2() {
        return "doctor/caseInfo/case_list2";
    }
    //    获取在诊病历列表
    @ResponseBody
    @RequestMapping("/findAll1")
    public LayData findAll1(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return caseInfoService.findByDocId(userId);
    }
    //    获取历史病历列表
    @ResponseBody
    @RequestMapping("/findAll2")
    public LayData findAll2(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return caseInfoService.findByDocIdR(userId);
    }
    @ResponseBody
    @RequestMapping("/findByName")
    public LayData findByName(String diagnoseName){
        LayData byLikeName = caseInfoService.findByLikeName(diagnoseName);
        return byLikeName;
    }
    @ResponseBody
    @RequestMapping("/getById")
    public LayData getById(String caseId){
        LayData byId = caseInfoService.getById(caseId);
        return byId;
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
        model.setViewName("doctor/caseInfo/case_info");
        model.addObject("caseInfos", caseInfos);
        return model;
    }
    // 去新增病例界面
    @RequestMapping("/toCaseAdd")
    public ModelAndView caseAdd() {
        ModelAndView model = new ModelAndView();
        List<Customer> allCus = customerService.findAllCus();
        Integer count = caseInfoService.getCount();
        model.setViewName("doctor/caseInfo/case_add");
        model.addObject("caseNum", count);
        model.addObject("cusList", allCus);
        return model;
    }

    @RequestMapping("/adminToCaseAdd")
    public ModelAndView adminToCaseAdd() {
        ModelAndView model = new ModelAndView();
        List<Customer> allCus = customerService.findAllCus();
        List<Doctor> allDoc = doctorService.findAllDoc();
        Integer count = caseInfoService.getCount();
        model.setViewName("admin/doctor/case_add");
        model.addObject("caseNum", count);
        model.addObject("cusList", allCus);
        model.addObject("docList", allDoc);
        return model;
    }
    // 新增病例
    @ResponseBody
    @RequestMapping("/saveCase")
    public Integer saveCase(@RequestBody CaseInfo caseInfo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        caseInfo.setDoctorId(userId);
        caseInfo.setTrResult(1);
        caseInfo.setStatus(1);
        System.out.println("获取到的CaseInfo信息：" + caseInfo);
        Integer index = caseInfoService.save(caseInfo);
        System.out.println("病例添加结果：" + index);
        return 1;
    }

    @ResponseBody
    @RequestMapping("/adminSaveCase")
    public Integer adminSaveCase(@RequestBody CaseInfo caseInfo) {
        caseInfo.setStatus(1);
        caseInfo.setTrResult(1);
        Integer index = caseInfoService.save(caseInfo);
        System.out.println("病例添加结果：" + index);
        Integer caseNum = caseInfoService.getCount();
        return caseNum;
    }

    @RequestMapping("/toCaseEdit")
    public ModelAndView toCaseEdit(String caseId){
        ModelAndView model = new ModelAndView();
        model.setViewName("doctor/caseInfo/case_edit");
        CaseInfo byId = caseInfoService.findById(caseId);
        model.addObject("caseInfo",byId);
        return model;
    }

    @ResponseBody
    @RequestMapping("/updateCase")
    public Integer updateCase(@RequestBody CaseInfo caseInfo){
        return caseInfoService.updateByCaseId(caseInfo);
    }

    @RequestMapping("/toRevisit")
    public ModelAndView toRevisit(String caseId, String customerId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("doctor/caseInfo/case_revisit");
        model.addObject("caseId", caseId);
        model.addObject("cusId", customerId);
        return model;
    }

    //    跳转到添加复诊信息界面
    @RequestMapping("/adminToRevisit")
    public ModelAndView adminToRevisit(String caseId, String customerId) {
        ModelAndView model = new ModelAndView();
        List<Doctor> allDoc = doctorService.findAllDoc();
        model.setViewName("admin/doctor/case_revisit");
        model.addObject("caseId", caseId);
        model.addObject("docList", allDoc);
        model.addObject("cusId", customerId);
        return model;
    }

    @ResponseBody
    @RequestMapping("/saveReVisit")
    public Integer saveReVisit(@RequestBody CaseInfo caseInfo, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        caseInfo.setStatus(2);
        caseInfo.setTrResult(0);
        caseInfo.setDoctorId(userId);
        System.out.println("获取到的新增复诊病例：" + caseInfo);
        return caseInfoService.save(caseInfo);
    }

    @ResponseBody
    @RequestMapping("/adminSaveReVisit")
    public Integer adminSaveReVisit(@RequestBody CaseInfo caseInfo) {
        caseInfo.setStatus(2);
        caseInfo.setTrResult(0);
        System.out.println("获取到的新增复诊病例：" + caseInfo);
        Integer index = caseInfoService.save(caseInfo);
        System.out.println("病例添加结果：" + index);
        CaseInfo byId = caseInfoService.findById(caseInfo.getCaseId());
        byId.setTrResult(byId.getTrResult()+1);
        Integer update = caseInfoService.updateByCaseId(byId);
        System.out.println("病例更新结果：" + update);
        return caseInfoService.getCount();
    }

    @ResponseBody
    @RequestMapping("/finishCase")
    public Integer finishCase(@RequestBody CaseInfo caseInfo){
        caseInfo.setStatus(3);
        System.out.println("终止治疗：" + caseInfo);
        return caseInfoService.updateByCaseId(caseInfo);
    }

    @ResponseBody
    @GetMapping("/delCase")
    public Integer delCase(String caseId, String customerId, String diagnoseTime){
        CaseInfo byId = caseInfoService.findById(caseId);
        byId.setTrResult(byId.getTrResult()-1);
        caseInfoService.updateByCaseId(byId);
        return caseInfoService.delByAllId(caseId,customerId,diagnoseTime);
    }

    /*管理员模块*/
    @RequestMapping("/adminToList")
    public String adminToList() {
        return "admin/doctor/case_list";
    }

    @ResponseBody
    @GetMapping("/delByCaseId")
    public Integer delByCaseId(String caseId){
        return caseInfoService.deleteByCaseId(caseId);
    }

    @ResponseBody
    @RequestMapping("/adminFindAll")
    public LayData adminFindAll(Integer page,Integer limit) {
        return caseInfoService.findAll(page,limit);
    }
}