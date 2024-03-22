package com.project.repository.business;

import com.project.entity.concretes.business.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo,Long> {
    List<PatientInfo> getAllByPatientId_Id(Long studentId);

   boolean existsByIdEquals(Long id);

   @Query("SELECT (count(p)>0) from PatientInfo p WHERE p.patient.id=?1")
    boolean existsByPatient_IdEquals(Long patientId);

   @Query("SELECT p FROM PatientInfo p WHERE p.patient.id=?1")
    List<PatientInfo> findByPatient_IdEquals(Long patientId);

    @Query("SELECT p FROM PatientInfo p WHERE p.doctor.id=?1")
    Page<PatientInfo> findByDoctorId_UsernameEquals(String userName, Pageable pageable);

    @Query("SELECT p FROM PatientInfo p WHERE p.patient.username=?1")
    Page<PatientInfo> findByPatientId_UsernameEquals(String userName, Pageable pageable);
}
