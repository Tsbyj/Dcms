package com.dcms.service;

import com.dcms.pojo.cases.CaseInfo;
import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.login.LoginInfo;
import com.dcms.pojo.other.LayData;
import com.dcms.repository.DoctorRepository;
import com.dcms.repository.LoginInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.Doc;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/22 0022 15:04
 * Description:
 **/
@Service
public class DoctorService {
    @Resource
    private DoctorRepository doctorRepository;
    @Resource
    private LoginInfoRepository loginInfoRepository;

    public Doctor findById(String docId){
        return doctorRepository.findById(docId);
    }

    public List<Doctor> findByName(String docName){
        return doctorRepository.findByName(docName);
    }

    public List<Doctor> findAllDoc() {
        return doctorRepository.findAllDoc();
    }

    public Integer getCount(String loginTime){
        return doctorRepository.getCount(loginTime);
    }

    public Integer updateDoc(Doctor doctor){
        return doctorRepository.update(doctor);
    }

    /*客户模块*/
    public List<Doctor> showDoc(){
        List<Doctor> docList = new ArrayList<>();
        Integer counts = doctorRepository.getCounts();
        List<Doctor> all = doctorRepository.findAll(0,counts);
        if(all.size() > 4){
            for(int i = 0;i < 5;i++){
                String name = all.get(i).getDocTitle() + ":" + all.get(i).getDocName();
                Doctor doc = new Doctor();
                doc.setDocId("/img/doc/" + all.get(i).getDocId() + ".bmp");
                doc.setDocName(name);
                docList.add(doc);
            }
        }else if(all.size() > 0) {
            for (Doctor doctor : all) {
                String name = doctor.getDocTitle() + ":" + doctor.getDocName();
                Doctor doc = new Doctor();
                doc.setDocId(doctor.getDocId());
                docList.add(doc);
            }
        }
        return docList;
    }

    public List<Doctor> showAllDoc(){
        Integer counts = doctorRepository.getCounts();
        List<Doctor> all = doctorRepository.findAll(0, counts);
        for (Doctor doctor : all) {
            doctor.setCid("/img/doc/" + doctor.getDocId() + ".bmp");
        }
        return all;
    }
    /*管理员模块*/
    public LayData findAll(Integer page, Integer limit) {
        LayData docList = new LayData();
        List<Doctor> all = doctorRepository.findAll(((page - 1) * limit), limit);
        Integer counts = doctorRepository.getCounts();
        docList.setCode(0);
        if (all.size() > 0) {
            docList.setData(all);
            docList.setMsg("所有医生信息！");
            docList.setCount(counts);
        }
        return docList;
    }
    public Integer saveDoc(Doctor doctor){
        doctor.setDocId(getDoctorId());
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(doctor.getDocId());
        loginInfo.setUsername(doctor.getDocName());
        loginInfo.setPassword("123123");
        loginInfo.setUserSort(2);
        Integer save = loginInfoRepository.save(loginInfo);
        System.out.println("医生 账号信息存储状态：" + save);
        return doctorRepository.save(doctor);
    }

    public Integer delById(String docId){
        Integer index = loginInfoRepository.deleteById(docId);
        System.out.println("医生 账号信息删除结果：" + index);
        return doctorRepository.deleteById(docId);
    }

    public String upload(MultipartFile file, String path, String fileName, String userId) throws Exception {
        System.out.println("上传图片方法中，医生ID：" + userId);
        // 生成新的文件名
        String realPath = path + "/" + userId + fileName.substring(fileName.lastIndexOf("."));
        File dest = new File(realPath);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        // 保存文件
        file.transferTo(dest);
        return dest.getName();
    }

    // 自动生成下一医生ID
    public String getDoctorId() {
        String NextDoctorId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下已注册的客户数量
        Integer count = doctorRepository.getCount(s1);
        // 改数量加1，获取下一个医生的ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextDoctorId = NextDoctorId + "0";
            }
            NextDoctorId = s1 + "D" + NextDoctorId + count;
        }
        while (true){
            Doctor byId = doctorRepository.findById(NextDoctorId);
            System.out.println("----------:" + byId);
            if(byId == null){
                break;
            }else {
                String pro = NextDoctorId.substring(0, NextDoctorId.length() - length);
                String end = NextDoctorId.substring(NextDoctorId.length() - length);
                Integer num = Integer.parseInt(end);
                num++;
                NextDoctorId = pro + num;
            }
        }

        return NextDoctorId;
    }
}
