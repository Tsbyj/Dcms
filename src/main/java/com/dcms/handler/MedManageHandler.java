package com.dcms.handler;

import com.dcms.pojo.med.MedManage;
import com.dcms.pojo.other.LayData;
import com.dcms.service.MedMService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/4 0004 20:00
 * Description:
 **/
@Controller
@RequestMapping("/medm")
public class MedManageHandler {
    @Resource
    private MedMService medMService;
    @ResponseBody
    @RequestMapping("/findByMedId")
    public LayData findByMedId(String medicineId){
        LayData allByMedId = medMService.findAllByMedId(medicineId);
        return allByMedId;
    }
    @RequestMapping("/toAdd")
    public ModelAndView toAdd(String medicineId, String medName){
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/medicine/medm_add");
        model.addObject("medId",medicineId);
        model.addObject("medName",medName);
        return model;
    }

    @RequestMapping("/medInfo")
    public ModelAndView medInfo(String medId) {
        ModelAndView model = new ModelAndView();
        System.out.println("获取到的medId为：" + medId);
        List<MedManage> medMList = medMService.findByMedId(medId);
        model.setViewName("admin/medicine/med_info");
        model.addObject("medMList", medMList);
        model.addObject("medId", medId);
        return model;
    }

    @ResponseBody
    @RequestMapping("/saveMedM")
    public Integer saveMedM(@RequestBody MedManage medManage) {
        System.out.println(medManage);
        Integer index = medMService.saveMedManager(medManage);
        return index;
    }

    @ResponseBody
    @GetMapping("/deleteByProId")
    public Integer deleteByProId(String produceNum){
        Integer index = medMService.deleteByProId(produceNum);
        return index;
    }

    @ResponseBody
    @RequestMapping("/updateMedM")
    public Integer updateMedM(@RequestBody MedManage medManage){
        System.out.println(medManage);
        Integer index = medMService.updateMedMByProNum(medManage);
        return index;
    }

    @ResponseBody
    @RequestMapping("/findByType")
    public LayData<MedManage> findByType(String medType){
        return medMService.findMedMByType(medType);
    }
}
