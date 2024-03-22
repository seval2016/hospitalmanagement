package com.project.repository.business;

import com.project.entity.concretes.business.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo,Long> {
    List<PatientInfo> getAllByPatientId_Id(Long studentId);

   boolean existsByIdEquals(Long id);
}
