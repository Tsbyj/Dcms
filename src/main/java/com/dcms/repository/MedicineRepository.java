package com.dcms.repository;

import com.dcms.pojo.med.Medicine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository {
    List<Medicine> findAll(Integer page, Integer limit);

    List<Medicine> findMedByType(String medType);

    List<Medicine> findMedByName(String medName);

    Medicine findMedById(String medicineId);

    Integer updateMed(Medicine medicine);

    Integer saveMed(Medicine medicine);

    Integer deleteMedById(String medicineId);

    Integer getMedCount(String medTime);

    Integer getCount();
}
