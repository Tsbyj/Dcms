package com.dcms.repository;

import com.dcms.pojo.doc.Doctor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository {
    List<Doctor> findAll(Integer page, Integer limit);

    Doctor findById(String docId);

    List<Doctor> findByName(String docName);

    Integer save(Doctor doctor);

    Integer getCount(String loginTime);

    Integer update(Doctor doctor);

    Integer deleteById(String docId);

    Integer getCounts();

    List<Doctor> findAllDoc();
}
