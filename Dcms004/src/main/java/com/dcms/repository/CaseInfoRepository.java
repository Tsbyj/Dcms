package com.dcms.repository;

import com.dcms.pojo.cases.CaseInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseInfoRepository {
    List<CaseInfo> findByDocId(String doctorId);

    List<CaseInfo> findByDocIdR(String doctorId);

    List<CaseInfo> findByCusIdR(String customerId);

    CaseInfo findById(String caseId);

    Integer getRevisitNum(String caseId);
    /*以下为管理员模块所需方法，开发完可删除*/
    List<CaseInfo> findAll(Integer page, Integer limit);

    Integer save(CaseInfo caseInfo);

    Integer updateByCaseId(CaseInfo caseInfo);

    Integer deleteByCaseId(String caseId);

    List<CaseInfo> findThanResult(Double result);

    Integer getCount();

    Integer delByAllId(String caseId, String customerId, String diagnoseTime);

    //    获取当天新增的ID数量
    Integer getIdCount(String caseTime);

    //  根据病例Id获取该病例所有的复诊数据
    List<CaseInfo> getReVisit(String caseId);

    // 根据就诊时间查询复诊记录
    CaseInfo findByDiagnoseTime(String diagnoseTime, String caseId, String customerId);

    // 获取该ID一共有几天记录
    Integer getIdNum(String caseId);

    // 根据名字模糊查询
    List<CaseInfo> findByLikeName(String diagnoseLike);
}
