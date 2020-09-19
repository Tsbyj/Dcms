package com.dcms.service;

import com.dcms.pojo.other.LayData;
import com.dcms.pojo.cases.CaseInfo;
import com.dcms.pojo.cases.CaseInfos;
import com.dcms.pojo.cus.Customer;
import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.med.MedManage;
import com.dcms.pojo.med.MedUserRecord;
import com.dcms.repository.CaseInfoRepository;
import com.dcms.repository.CustomerRepository;
import com.dcms.repository.DoctorRepository;
import com.dcms.repository.MedManageRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/9 0009 22:48
 * Description:
 **/
@Service
public class CaseInfoService {
    @Resource
    private CaseInfoRepository caseInfoRepository;
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private DoctorRepository doctorRepository;
    @Resource
    private MedRecordService medRecordService;

    @Resource
    private MedManageRepository medManageRepository;
    //根据医生ID查询该医生在诊病例
    public LayData findByDocId(String doctorId){
        LayData layData = new LayData();
        List<CaseInfo> byDocId = caseInfoRepository.findByDocId(doctorId);
        // 获取该病例的复诊次数
        for (CaseInfo c:byDocId) {
            Integer num = caseInfoRepository.getRevisitNum(c.getCaseId());
            c.setRevisitNum(num);
        }
        if (byDocId.size() > 0){
            layData.setCode(0);
            layData.setCount(byDocId.size());
            layData.setMsg("在诊病例列表");
            layData.setData(byDocId);
        }else {
            layData.setCode(0);
        }
        return layData;
    }
    //根据医生ID查询该医生已完成病例
    public LayData findByDocIdR(String doctorId){
        LayData layData = new LayData();
        List<CaseInfo> byDocId = caseInfoRepository.findByDocIdR(doctorId);
        for (CaseInfo c:byDocId) {
            Integer num = caseInfoRepository.getRevisitNum(c.getCaseId());
            c.setRevisitNum(num);
        }
        if (byDocId.size() > 0){
            layData.setCode(0);
            layData.setCount(byDocId.size());
            layData.setMsg("在诊病例列表");
            layData.setData(byDocId);
        }else {
            layData.setCode(0);
        }
        return layData;
    }
    //根据客户ID查询病例
    public LayData findByCusIdR(String customerId){
        LayData layData = new LayData();
        List<CaseInfo> byCusId = caseInfoRepository.findByCusIdR(customerId);
        for (CaseInfo c:byCusId) {
            Integer num = caseInfoRepository.getRevisitNum(c.getCaseId());
            c.setRevisitNum(num);
        }
        if (byCusId.size() > 0){
            layData.setCount(byCusId.size());
            layData.setMsg("该客户所有病例列表");
            layData.setData(byCusId);
            layData.setCode(0);
        }else {
            layData.setCode(0);
        }
        return layData;
    }

    //添加药品记录
    public Integer save(CaseInfo caseInfo) {
        if(caseInfo.getCaseId() == null || caseInfo.getCaseId().equals("")){
            caseInfo.setCaseId(getCaseId()[0]);
        }
        if(caseInfo.getDiagnoseTime() == null || caseInfo.getDiagnoseTime().equals("")){
            caseInfo.setDiagnoseTime(getCaseId()[1]);
        }
        Integer save = caseInfoRepository.save(caseInfo);
        if (save == 1){
            List<MedUserRecord> medU = getMedU(caseInfo.getMedMess(), getCaseId()[0]);
            for (MedUserRecord m:medU) {
                Integer index = medRecordService.saveRecord(m);
                MedManage byProId = medManageRepository.findByProId(m.getMedmId());
                Integer newNum = byProId.getMedNumber() - m.getUseNum();
                byProId.setMedNumber(newNum);
                Integer index1 = medManageRepository.updateNumByNo(byProId);
                System.out.println("药品记录新增结果： " + index + "药品库存更新结果： " + index1);
            }
        }
        return save;
    }

    public Integer getCount() {
        Integer count = caseInfoRepository.getCount();
        return count;
    }

    // 自动生成下一病例ID
    private String[] getCaseId() {
        String[] nextCaseIds = new String[2];
        String NextCaseId = "";
        // 获取今天日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取今天已添加的病例数量
        Integer count = caseInfoRepository.getIdCount(s1);
        // 改数量加1，获取下一个药品的ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextCaseId = NextCaseId + "0";
            }
            NextCaseId = s1 + "C" + NextCaseId + count;
        }
        while (true){
            CaseInfo byId = caseInfoRepository.findById(NextCaseId);
            System.out.println("----------:" + byId);
            if(byId == null){
                break;
            }else {
                String pro = NextCaseId.substring(0, NextCaseId.length() - length);
                String end = NextCaseId.substring(NextCaseId.length() - length);
                Integer num = Integer.parseInt(end);
                num++;
                NextCaseId = pro + num;
            }
        }
        nextCaseIds[0] = NextCaseId;
        nextCaseIds[1] = s1;
        return nextCaseIds;
    }

    //处理药品用量信息
    private List<MedUserRecord> getMedU(String medMess, String caseId){
        List<MedUserRecord> mList = new ArrayList<>();
        String[] split = medMess.split("\\|");
        for(int i = 0;i < split.length-1;i++){
            String[] split1 = split[i].split(":");
            MedUserRecord mm = new MedUserRecord();
            mm.setMedmId(split1[0].trim());
            mm.setUseNum(Integer.parseInt(split1[1].trim()));
            mm.setCaseId(caseId);
            mList.add(mm);
        }
        return mList;
    }

    public Integer updateByCaseId(CaseInfo caseInfo) {
        return caseInfoRepository.updateByCaseId(caseInfo);
    }
    //  获取病例关联的全部信息
    public CaseInfos findOneAllMess(String caseId) {
        CaseInfos caseInfos = new CaseInfos();
        //  通过Id获取病例对象
        CaseInfo caseInfo = caseInfoRepository.findById(caseId);
        //  根据 ID获取客户对象
        Customer customer = customerRepository.findById(caseInfo.getCustomerId());
        //  根据 ID获取医生对象
        Doctor doctor = doctorRepository.findById(caseInfo.getDoctorId());
        caseInfos.setCaseInfo(caseInfo);
        caseInfos.setCustomer(customer);
        caseInfos.setDoctor(doctor);
        return caseInfos;
    }

    /*管理员模块*/
    public LayData findAll(Integer page, Integer limit) {
        LayData caseList = new LayData();
        List<CaseInfo> all = caseInfoRepository.findAll(((page - 1) * limit), limit);
        Integer count = caseInfoRepository.getCount();
        caseList.setMsg("所有病例信息");
        caseList.setCount(count);
        caseList.setData(all);
        if (all != null) {
            caseList.setCode(0);
        } else {
            caseList.setCode(500);
        }
        return caseList;
    }

    public CaseInfo findById(String caseId) {
        return caseInfoRepository.findById(caseId);
    }

    public Integer deleteByCaseId(String caseId) {
        return caseInfoRepository.deleteByCaseId(caseId);
    }

    public List<CaseInfo> findThanResult(Double result) {
        return caseInfoRepository.findThanResult(result);
    }

    public Integer getIdNum(String caseId){
        return caseInfoRepository.getIdNum(caseId);
    }
    // 根据ID获取所有复诊信息
    public List<CaseInfos> findReVisit(String caseId){
        List<CaseInfos> casesList = new ArrayList<>();
        List<CaseInfo> revisitList = caseInfoRepository.getReVisit(caseId);
        for (CaseInfo caseInfo:revisitList) {
            CaseInfo byDiagnoseTime = caseInfoRepository.findByDiagnoseTime(caseInfo.getDiagnoseTime(),caseInfo.getCaseId(),caseInfo.getCustomerId());
            CaseInfos oneAllMess = new CaseInfos();
            Doctor byId = doctorRepository.findById(byDiagnoseTime.getDoctorId());
            oneAllMess.setDoctor(byId);
            oneAllMess.setCaseInfo(byDiagnoseTime);
            casesList.add(oneAllMess);
        }
        return casesList;
    }

    //    获取下一个新增病例ID
    public Integer getIdCount(String caseTime) {
        return caseInfoRepository.getIdCount(caseTime);
    }

    // 根据病例名查找数据
    public LayData findByLikeName(String diagnoseLike){
        LayData all = new LayData();
        List<CaseInfo> byLikeName = caseInfoRepository.findByLikeName(diagnoseLike);
        all.setCode(0);
        if(byLikeName != null){
            all.setData(byLikeName);
            all.setMsg("按病例名查找结果");
            all.setCount(byLikeName.size());
        }
        return all;
    }

    public LayData getById(String caseId){
        LayData all = new LayData();
        List<CaseInfo> list = new ArrayList<>();
        CaseInfo byId = caseInfoRepository.findById(caseId);
        all.setCode(0);
        if(byId != null){
            all.setMsg("按ID查找结果");
            list.add(byId);
            all.setData(list);
            all.setCount(1);
        }
        return all;
    }

    public Integer delByAllId(String caseId, String customerId, String diagnoseTime){
        return caseInfoRepository.delByAllId(caseId,customerId,diagnoseTime);
    }
}
