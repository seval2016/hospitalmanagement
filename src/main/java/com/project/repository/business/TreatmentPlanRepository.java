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

    //Aşağaıdaki Query'nin anlamı TreatmentPlan tablosunda User tablosundada olan username'i alttaki metodun parametresindeki değere eşit olan user'ın treatment planlarını getir.
    @Query("SELECT t FROM TreatmentPlan t INNER JOIN t.users users WHERE users.username=?1")
    Set<TreatmentPlan> getTreatmentPlanByUsersUsername(String userName);

    Set<TreatmentPlan> findByUsers_IdEquals(Long doctorId);


    // SQL --> SELECT * FROM treatmen_plan WHERE treatmen_plan.id IN (2,3)
    @Query("SELECT t FROM TreatmentPlan t WHERE t.id IN :departmentIdSet")
    Set<TreatmentPlan> getTreatmentPlanByPlanIdList(Set<Long> departmentIdSet);
}
