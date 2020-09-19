package com.dcms.service;

import com.dcms.pojo.other.LayData;
import com.dcms.pojo.med.MedManage;
import com.dcms.pojo.med.Medicine;
import com.dcms.repository.MedManageRepository;
import com.dcms.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/21 0021 16:51
 * Description:
 **/
@Service
public class MedMService {
    @Resource
    private MedManageRepository medManageRepository;
    @Resource
    private MedicineRepository medicineRepository;
    //按种类获取药品批次信息
    public LayData<MedManage> findMedMByType(String medType){
        LayData<MedManage> layData = new LayData<>();
        layData.setCode(0);
        List<MedManage> medmList = new ArrayList<>();
        List<Medicine> medByType = medicineRepository.findMedByType(medType);
        for (Medicine m:medByType) {
            List<MedManage> byMedId = medManageRepository.findByMedId(m.getMedicineId());
            for (MedManage mm:byMedId) {
                mm.setMedName(m.getMedName());
            }
            medmList.addAll(byMedId);
        }
        if(medmList.size() > 0){
            layData.setMsg("按药品种类获取的药品批次信息");
            layData.setData(medmList);
            layData.setCount(medmList.size());
        }
        return layData;
    }

    MedManage findByProId(String proId){
        return medManageRepository.findByProId(proId);
    }

    public Integer updateNumByNo(MedManage medManage){
        medManage.setMedTime(getMedMId()[1]);
        return medManageRepository.updateNumByNo(medManage);
    }
    /*管理员模块*/
    public LayData findAllByMedId(String medicineId) {
        LayData lay = new LayData();
        List<MedManage> allMedM = medManageRepository.findByMedId(medicineId);
        if(allMedM.size() > 0){
            lay.setData(allMedM);
            lay.setMsg("药品批次信息");
            lay.setCount(allMedM.size());
            lay.setCode(0);
        }else {
            lay.setCode(0);
        }
        return lay;
    }
    public List<MedManage> findByMedId(String medicineId) {
        return medManageRepository.findByMedId(medicineId);
    }

    public Integer saveMedManager(MedManage medManage) {
        medManage.setProduceNum(getMedMId()[0]);
        medManage.setMedTime(getMedMId()[1]);
        return medManageRepository.saveMedManager(medManage);
    }

    public Integer deleteByProId(String produceNumId){
        return medManageRepository.deleteByProId(produceNumId);
    }

    public Integer updateMedMByProNum(MedManage medManage) {
        medManage.setMedTime(getMedMId()[1]);
        return medManageRepository.updateMedMByProNum(medManage);
    }
    // 自动生成下一要药品产品批号ID
    private String[] getMedMId() {
        String[] ss = new String[2];
        String NextMedMId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下一注册的批号数量
        Integer count = medManageRepository.getMedMCount(s1);
        // 改数量加1，获取下一个药品批号的ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextMedMId = NextMedMId + "0";
            }
            NextMedMId = s1 + "P" + NextMedMId + count;
        }
        ss[0] = NextMedMId;
        ss[1] = s1;
        return ss;
    }
}
