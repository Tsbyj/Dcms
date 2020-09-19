package com.dcms.handler;

import com.dcms.pojo.med.MedUserRecord;
import com.dcms.pojo.med.Medicine;
import com.dcms.pojo.other.LayData;
import com.dcms.service.MedRecordService;
import com.dcms.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/2 0002 16:29
 * Description:
 **/
@Controller
@RequestMapping("/med")
public class MedicineHandler {
    @Autowired
    private MedicineService medicineService;
    @Resource
    private MedRecordService medRecordService;

    @RequestMapping("/toList")
    public String index() {
        return "admin/medicine/med_list";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "admin/medicine/med_add";
    }

    @ResponseBody
    @RequestMapping("/findAll")
    public LayData findAll(Integer page,Integer limit) {
        LayData medList = medicineService.findAll(page,limit);
        return medList;
    }

    @ResponseBody
    @RequestMapping("/saveMed")
    public Integer saveMed(@RequestBody Medicine medicine) {
//        System.out.println(medicine);
        Integer index = medicineService.saveMed(medicine);
        return index;
    }

    @RequestMapping("/toMedInfo")
    public ModelAndView toMedMInfo(String medicineId){
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/medicine/med_info");
        Medicine byMedId = medicineService.findByMedId(medicineId);
        model.addObject("med",byMedId);
        return model;
    }

    @RequestMapping("/toMedIndex")
    public ModelAndView toMedIndex(){
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/medicine/med_index");
        return model;
    }

    @ResponseBody
    @RequestMapping("/medIndex")
    public Map<String,String[]> findLastOne(){
        return medRecordService.getAll();
    }

    @RequestMapping("/toMedEdit")
    public ModelAndView toMedEdit(String medicineId){
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/medicine/med_edit");
        Medicine byMedId = medicineService.findByMedId(medicineId);
        model.addObject("med",byMedId);
        return model;
    }
    @ResponseBody
    @RequestMapping("/updateMed")
    public Integer updateMed(@RequestBody Medicine medicine){
//        System.out.println(medicine);
        Integer index = medicineService.updateMed(medicine);
        return index;
    }

    // 按ID搜索医生信息
    @ResponseBody
    @GetMapping("/adminFindById")
    public LayData adminFindById(String medicineId){
        LayData layData = new LayData();
        layData.setCode(0);
        if(medicineId != null && !medicineId.equals("")){
            Medicine byMedId = medicineService.findByMedId(medicineId);
            if(byMedId != null) {
                List<Medicine> list = new ArrayList<>();
                list.add(byMedId);
                layData.setData(list);
                layData.setCount(1);
                layData.setMsg("按ID查询");
            }
        }
        return layData;
    }

    // 按Name搜索医生信息
    @ResponseBody
    @GetMapping("/adminFindByName")
    public LayData adminFindByName(String medName){
        return medicineService.findMedByName(medName);
    }
    @ResponseBody
    @RequestMapping("/findByType")
    public LayData<Medicine> findByType(String medType){
        return medicineService.findByType(medType);
    }
}
