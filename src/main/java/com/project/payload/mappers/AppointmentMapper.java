package com.project.payload.mappers;

import com.project.entity.concretes.business.Appointment;
import com.project.payload.request.business.AppointmentRequest;
import com.project.payload.response.business.AppointmentResponse;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment mapAppointmentRequestToAppointment(AppointmentRequest appointmentRequest){
        return  Appointment.builder()
                .appointmentDetails(appointmentRequest.getAppointmentDetails())
                .appointmentDateTime(appointmentRequest.getAppointmentDateTime())
                .build();
    }

    public AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment){
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .appointmentDetails(appointment.getAppointmentDetails())
                .doctorId(appointment.getDoctor().getId())
                .patient(appointment.getPatient())
                .doctorName(appointment.getDoctor().getName())
                .build();
    }
    public Appointment mapAppointmentUpdateRequestToAppointment(AppointmentRequest appointmentRequest,Long appointmentId){
        return Appointment.builder()
                .id(appointmentId)
                .appointmentDetails(appointmentRequest.getAppointmentDetails())
                .appointmentDateTime(appointmentRequest.getAppointmentDateTime())
                .build();
    }
}
