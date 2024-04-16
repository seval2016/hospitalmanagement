package com.project.repository.business;

import com.project.entity.concretes.business.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("SELECT m FROM MedicalRecord m WHERE EXTRACT(YEAR FROM m.startDate) = ?1")
    List<MedicalRecord> findByYear(int year);
}
