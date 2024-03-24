package com.project.payload.mappers;

import com.project.entity.concretes.business.Appointment;
import com.project.payload.request.business.AppointmentRequest;
import com.project.payload.response.business.AppointmentResponse;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment mapAppointmentRequestToAppointment(AppointmentRequest appointmentRequest){
        return  Appointment.builder()
                .date(appointmentRequest.getDate())
                .startTime(appointmentRequest.getStartTime())
                .stopTime(appointmentRequest.getStopTime())
                .description(appointmentRequest.getDescription())
                .build();
    }

    public AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment){
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .date(appointment.getDate())
                .startTime(appointment.getStartTime())
                .stopTime(appointment.getStopTime())
                .description(appointment.getDescription())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getName())
                .patientId(appointment.getPatient().getId())
                .build();
    }
    public Appointment mapAppointmentUpdateRequestToAppointment(AppointmentRequest appointmentRequest,Long appointmentId){
        return Appointment.builder()
                .id(appointmentId)
                .date(appointmentRequest.getDate())
                .startTime(appointmentRequest.getStartTime())
                .stopTime(appointmentRequest.getStopTime())
                .description(appointmentRequest.getDescription())
                .build();
    }
}
