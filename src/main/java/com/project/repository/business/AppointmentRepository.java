package com.project.repository.business;

import com.project.entity.concretes.business.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByPatient_IdEquals(Long doctorId);

    List<Appointment> getByDoctor_IdEquals(Long patientId);
}
