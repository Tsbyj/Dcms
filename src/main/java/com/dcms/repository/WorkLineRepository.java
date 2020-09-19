package com.dcms.repository;

import com.dcms.pojo.other.WorkLine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkLineRepository {
    Integer save(WorkLine workLine);
    // 获取当前日期下的workId数量
    Integer getCount(String workIdPrefix);
    //获取所有未就诊（workFlag=3）的该医生的预约信息
    List<WorkLine> findNByDocId(String docId);
    //根据客户ID获取预约信息
    List<WorkLine> findNByCusId(String customerId);

    Integer updateFlag(WorkLine workLine);
}
