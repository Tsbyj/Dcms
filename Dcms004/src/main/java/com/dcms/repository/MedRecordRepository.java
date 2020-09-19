package com.dcms.repository;

import com.dcms.pojo.med.MedUserRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedRecordRepository {
    MedUserRecord findLastOne();
    List<MedUserRecord> findAll();
    Integer saveRecord(MedUserRecord medUserRecord);
    Integer getMedRCount(String nowTime);
}
