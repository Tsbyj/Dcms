package com.dcms.service;

import com.dcms.pojo.med.MedManage;
import com.dcms.pojo.med.MedUserRecord;
import com.dcms.pojo.med.Medicine;
import com.dcms.repository.MedManageRepository;
import com.dcms.repository.MedRecordRepository;
import com.dcms.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/22 0022 11:03
 * Description:
 **/
@Service
public class MedRecordService {
    @Resource
    private MedRecordRepository medRecordRepository;
    @Resource
    private MedManageRepository medManageRepository;
    @Resource
    private MedicineRepository medicineRepository;

    public Integer saveRecord(MedUserRecord medUserRecord){
        medUserRecord.setSerialNo(getMedRMId()[0]);
        medUserRecord.setUseTime(getMedRMId()[1]);
        return medRecordRepository.saveRecord(medUserRecord);
    }

    public Map<String,String[]> getAll(){
        Map<String,String[]> records = new HashMap<>();
        String[] re1 = {"手术用药","0","0","0","0","0","0"};
        String[] re2 = {"内服用药","0","0","0","0","0","0"};
        String[] re3 = {"外用用药","0","0","0","0","0","0"};
        String[] re4 = {"保健用药","0","0","0","0","0","0"};
        List<MedUserRecord> all = medRecordRepository.findAll();
        Set<String> medmId = new HashSet<>();
        Map<String,String> kind = new HashMap<>();
        // 获取不重复的批号ID
        for (MedUserRecord med : all) {
            medmId.add(med.getMedmId());
        }
        for (String s : medmId) {
            MedManage byProId = medManageRepository.findByProId(s);
            Medicine medById = medicineRepository.findMedById(byProId.getMedicineId());
            kind.put(s,medById.getMedType());
        }
        MedUserRecord lastOne = medRecordRepository.findLastOne();
        Map<Integer, Long> timeMap = getTimeMap(lastOne.getUseTime());
        for (MedUserRecord med : all) {
            String useTime = med.getUseTime();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Long date = s.parse(useTime).getTime();
                for (int i = 1; i < timeMap.size() + 1; i++) {
                    if(date > timeMap.get(i)){
                        for(Map.Entry<String,String> entry : kind.entrySet()){
                            if(med.getMedmId().equals(entry.getKey())){
                                switch (entry.getValue()) {
                                    case "S": {
                                        Integer num = Integer.parseInt(re1[i]);
                                        num = num + med.getUseNum();
                                        re1[i] = String.valueOf(num);
                                        break;
                                    }
                                    case "C1": {
                                        Integer num = Integer.parseInt(re2[i]);
                                        num = num + med.getUseNum();
                                        re2[i] = String.valueOf(num);
                                        break;
                                    }
                                    case "C2": {
                                        Integer num = Integer.parseInt(re3[i]);
                                        num = num + med.getUseNum();
                                        re3[i] = String.valueOf(num);
                                        break;
                                    }
                                    case "B1": {
                                        Integer num = Integer.parseInt(re4[i]);
                                        num = num + med.getUseNum();
                                        re4[i] = String.valueOf(num);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (ParseException e) {
                System.out.println("时间转化出错！");
                e.printStackTrace();
            }
        }
        records.put("one",re1);
        records.put("two",re2);
        records.put("three",re3);
        records.put("four",re4);
        return records;
    }

    public MedUserRecord findLastOne(){
        return medRecordRepository.findLastOne();
    }

    private Map<Integer,Long> getTimeMap(String useTime) {
        Map<Integer,Long> timeMap = new HashMap<>();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = s.parse(useTime);
            for (int i = 1; i < 7; i++) {
                Long date1 = date.getTime() - 7 * i * 24 * 60 * 60 * 1000L;
                timeMap.put(i,date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMap;
    }

    // 自动生成下一要药品使用流水号
    private String[] getMedRMId() {
        String[] ss = new String[2];
        String NextMedRId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下一注册的流水号数量
        Integer count = medRecordRepository.getMedRCount(s1);
        // 改数量加1，获取下一个药品批号的ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextMedRId = NextMedRId + "0";
            }
            NextMedRId = s1 + "S" + NextMedRId + count;
        }
        ss[0] = NextMedRId;
        ss[1] = s1;
        return ss;
    }
}
