package com.dcms.repository;

import com.dcms.pojo.med.MedManage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedManageRepository {
    List<MedManage> findAllMedM();

    List<MedManage> findByMedId(String medicineId);

    MedManage findByProId(String proId);

    Integer updateNumByNo(MedManage medManage);

    Integer updateMedMByProNum(MedManage medManage);

    Integer saveMedManager(MedManage medManage);

    Integer deleteByMedId(String medicineId);

    Integer deleteByProId(String produceNumId);

    Integer getMedMCount(String MedMTime);
}
