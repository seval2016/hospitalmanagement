package com.project.repository.business;

import com.project.entity.concretes.business.TreatmentPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    List<TreatmentPlan> findByUsers_IdNull();
    List<TreatmentPlan> findByUsers_IdNotNull();
    @Query("SELECT t FROM treatmentPlan t INNER JOIN t.users users WHERE users.username=?1")
    Set<TreatmentPlan> getTreatmentPlanByUsersUsername(String userName);

    Set<TreatmentPlan> findByUsers_IdEquals(Long doctorId);


    @Query("SELECT t FROM treatmentPlan t WHERE t.id IN :departmentIdSet")
    // SQL --> SELECT * FROM treatmen_plan WHERE treatmen_plan.id IN (2,3)
    Set<TreatmentPlan> getTreatmentPlanByPlanIdList(Set<Long> departmentIdSet);
}
