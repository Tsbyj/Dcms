package com.dcms.service;

import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.other.LayData;
import com.dcms.pojo.other.WorkLine;
import com.dcms.repository.DoctorRepository;
import com.dcms.repository.WorkLineRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/28 0028 21:37
 * Description:
 **/
@Service
public class WorkLineService {
    @Resource
    private WorkLineRepository workLineRepository;
    @Resource
    private DoctorRepository doctorRepository;

    public Integer saveNewWorkLine(WorkLine workLine){
        workLine.setWorkId(getWorkLineId());
        workLine.setWorkFlag(3);
        workLine.setRemake("未到预约时间！");
//        System.out.println("新增预约信息：" + workLine);
        return workLineRepository.save(workLine);
    }
    // 获取该医生所有预约信息
    public LayData findNByDocId(String docId){
        LayData layData = new LayData();
        List<WorkLine> nByDocId = workLineRepository.findNByDocId(docId);
        if(nByDocId.size() > 0){
            layData.setData(nByDocId);
            layData.setCount(nByDocId.size());
            layData.setCode(0);
            layData.setMsg("该医生所有预约信息");
        }else {
            layData.setCode(0);
        }
        return layData;
    }

    // 获取客户所有预约信息
    public LayData findNByCusId(String customerId){
        LayData layData = new LayData();
        List<WorkLine> nByCusId = workLineRepository.findNByCusId(customerId);
        for (WorkLine workLine : nByCusId) {
            Doctor byId = doctorRepository.findById(workLine.getDocId());
            workLine.setDocId(byId.getDocName());
        }
        if(nByCusId.size() > 0){
            layData.setData(nByCusId);
            layData.setCount(nByCusId.size());
            layData.setCode(0);
            layData.setMsg("该客户所有预约信息");
        }else {
            layData.setCode(0);
        }
        return layData;
    }

    public Integer updateFlag(WorkLine workLine){
        return workLineRepository.updateFlag(workLine);
    }
    // 自动生成下一预约ID
    private String getWorkLineId() {
        String nextWorkId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下已注册的客户数量
        Integer count = workLineRepository.getCount(s1);
        // 改数量加1，获取下一个客户的ID尾号
        count++;
        // 若下一编号ID长度不足4位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                nextWorkId = nextWorkId + "0";
            }
            nextWorkId = s1 + "W" + nextWorkId + count;
            System.out.println("下一个客户编号为：" + nextWorkId);
        }

        return nextWorkId;
    }
}
