package com.dcms.handler;

import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.other.LayData;
import com.dcms.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/4/18 0018 15:32
 * Description:
 **/
@Controller
@RequestMapping("/doctor")
public class DoctorHandler {
    @Resource
    DoctorService doctorService;

    @RequestMapping("/toDocWel")
    public String toCusWel(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if(userId.contains("D")){
            return "doctor/doc_wel";
        }else {
            return "redirect:/login/toDocLogin";
        }
    }

    @ResponseBody
    @RequestMapping("/delByDocId")
    public Integer delByDocId(String docId){
        String img_path = "D:/eaducation/Dcms004/src/main/resources/static/img/doc/" + docId + ".bmp";
        File file = new File(img_path);
        String resultInfo;
        if (file.exists()) {//文件是否存在
            if (file.delete()) {//存在就删了，返回1
                resultInfo =  "1";
            } else {
                resultInfo =  "0";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        System.out.println("图片删除结果：" + resultInfo);
        return doctorService.delById(docId);
    }
    @RequestMapping("/toDocInfo")
    public ModelAndView toDocInfo(HttpSession session){
        ModelAndView model = new ModelAndView();
        String userId = (String) session.getAttribute("userId");
        Doctor byId = doctorService.findById(userId);
        model.addObject("doc",byId);
        model.setViewName("doctor/doc_info");
        return model;
    }

    @RequestMapping("/toDocRePass")
    public String toRePass(){
        return "doctor/doc_mess";
    }
    /*管理员模块*/
    @RequestMapping("/adminToDocList")
    public String adminToDocList(){
        return "admin/doctor/doc_list";
    }

    @RequestMapping("/adminToAdd")
    public String adminToAdd(){
        return "admin/doctor/doc_add";
    }

    @ResponseBody
    @RequestMapping("/adminFindAllDoc")
    public LayData adminFindAllDoc(Integer page, Integer limit){
        return doctorService.findAll(page,limit);
    }

    /*管理员模块*/
    @RequestMapping("/toAddDoc")
    public String toAddDoc(){
        return "admin/doctor/doc_add";
    }

    @RequestMapping("/toDocEdit")
    public ModelAndView toDocEdit(String docId){
        ModelAndView model = new ModelAndView();
        Doctor byId = doctorService.findById(docId);
        model.addObject("doctor",byId);
        model.setViewName("admin/doctor/doc_edit");
        return model;
    }

    @ResponseBody
    @RequestMapping("/updateDoc")
    public Integer updateDoc(@RequestBody Doctor doctor){
        return doctorService.updateDoc(doctor);
    }

    @ResponseBody
    @RequestMapping("/saveDoc")
    public Integer saveDoc(@RequestBody Doctor doctor){
        return doctorService.saveDoc(doctor);
    }

    // 按ID搜索医生信息
    @ResponseBody
    @GetMapping("/adminFindById")
    public LayData adminFindById(String docId){
        LayData layData = new LayData();
        layData.setCode(0);
        if(docId != null && !docId.equals("")){
            Doctor byId = doctorService.findById(docId);
            if(byId != null) {
                List<Doctor> list = new ArrayList<>();
                list.add(byId);
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
    public LayData adminFindByName(String docName){
        LayData layData = new LayData();
        layData.setCode(0);
        List<Doctor> byName = doctorService.findByName(docName);
        if(byName.size() > 0){
            layData.setData(byName);
            layData.setCount(byName.size());
            layData.setMsg("按Name查询");
        }
        return layData;
    }

    @ResponseBody
    @RequestMapping("/fileUpload")
    public Map<String,Object> fileUpload(@RequestParam("file") MultipartFile file){
        Map<String,Object> map  = new HashMap<>();
        String userId = doctorService.getDoctorId();
        String uploadDir = "D:/eaducation/Dcms004/src/main/resources/static/img/doc/";
        try {
            // 图片路径
            String imgUrl = null;
            //上传
            String filename = doctorService.upload(file, uploadDir, Objects.requireNonNull(file.getOriginalFilename()), userId);
            if (filename != null) {
                imgUrl = new File(uploadDir).getName() + "/" + filename;
            }
            map.put("code",0);
            map.put("msg","");
            map.put("data",imgUrl);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",500);
            map.put("msg","上传失败");
            map.put("data",null);
            return map;
        }
    }
}
