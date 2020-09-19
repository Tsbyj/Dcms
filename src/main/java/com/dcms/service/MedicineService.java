package com.dcms.service;

import com.dcms.pojo.med.MedManage;
import com.dcms.pojo.med.Medicine;
import com.dcms.pojo.other.LayData;
import com.dcms.repository.MedManageRepository;
import com.dcms.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/21 0021 19:49
 * Description:
 **/
@Service
public class MedicineService {
    @Resource
    private MedicineRepository medicineRepository;
    @Resource
    private MedManageRepository medManageRepository;

    /*管理员模块*/
    public LayData findAll(Integer page,Integer limit){
        LayData<Medicine> medList = new LayData<>();
        medList.setCode(0);
        List<Medicine> all = medicineRepository.findAll(((page - 1) * limit),limit);
        for (Medicine m : all) {
            List<MedManage> byMedId = medManageRepository.findByMedId(m.getMedicineId());
            if(byMedId.size() > 0){
                m.setMedMCount(byMedId.size());
                Integer medNum = 0;
                for (MedManage mm : byMedId) {
                    medNum = medNum + mm.getMedNumber();
                }
                m.setMedNum(medNum);
                m.setMedTime(byMedId.get(byMedId.size()-1).getMedTime());
            }
        }
        Integer count = medicineRepository.getCount();
        if(all.size() > 0){
            medList.setMsg("所有药品信息");
            medList.setData(all);
            medList.setCount(count);
        }
        return medList;
    }

    public LayData<Medicine> findMedByName(String medName){
        LayData<Medicine> layData = new LayData<>();
        layData.setCode(0);
        List<Medicine> medByName = medicineRepository.findMedByName(medName);
        for (Medicine medicine : medByName) {
            List<MedManage> byMedId = medManageRepository.findByMedId(medicine.getMedicineId());
            if(byMedId.size() > 0){
                medicine.setMedMCount(byMedId.size());
                Integer medNum = 0;
                for (MedManage mm : byMedId) {
                    medNum = medNum + mm.getMedNumber();
                }
                medicine.setMedNum(medNum);
                medicine.setMedTime(byMedId.get(byMedId.size()-1).getMedTime());
            }
        }
        if(medByName.size() > 0){
            layData.setMsg("所有类似药名的药品信息");
            layData.setData(medByName);
            layData.setCount(medByName.size());
        }
        return layData;
    }

    public LayData<Medicine> findByType(String medType){
        LayData<Medicine> layData = new LayData<>();
        layData.setCode(0);
        List<Medicine> medByType = medicineRepository.findMedByType(medType);
        for (Medicine medicine : medByType) {
            List<MedManage> byMedId = medManageRepository.findByMedId(medicine.getMedicineId());
            if(byMedId.size() > 0){
                medicine.setMedMCount(byMedId.size());
                Integer medNum = 0;
                for (MedManage mm : byMedId) {
                    medNum = medNum + mm.getMedNumber();
                }
                medicine.setMedNum(medNum);
                medicine.setMedTime(byMedId.get(byMedId.size()-1).getMedTime());
            }
        }
        if(medByType.size() > 0){
            layData.setMsg("所有类似药名的药品信息");
            layData.setData(medByType);
            layData.setCount(medByType.size());
        }
        return layData;
    }

    public Integer saveMed(Medicine medicine){
        medicine.setMedicineId(getMedId());
        return medicineRepository.saveMed(medicine);
    }

    public Medicine findByMedId(String medicineId){
        return medicineRepository.findMedById(medicineId);
    }

    public Integer updateMed(Medicine medicine){
        return medicineRepository.updateMed(medicine);
    }

    // 自动生成下一药品ID
    private String getMedId(){
        String NextMedId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下已注册的客户数量
        Integer count = medicineRepository.getMedCount(s1);
        // 改数量加1，获取下一个药品的ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if(length < 3){
            int i = 3 - length;
            for (int j = 0; j < i ; j++) {
                NextMedId = NextMedId + "0";
            }
            NextMedId = s1 + "M" + NextMedId + count;
            System.out.println("下一个药品编号为：" + NextMedId);
        }
        return NextMedId;
    }
}
