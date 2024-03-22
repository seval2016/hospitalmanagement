package com.project.service.business;

import com.project.payload.request.business.AppointmentRequest;
import com.project.payload.response.business.AppointmentResponse;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    public ResponseMessage<AppointmentResponse> saveAppointment(HttpServletRequest httpServletRequest, AppointmentRequest appointmentRequest) {
        return null;
    }

    public List<AppointmentResponse> getAll() {
        return null;
    }

    public ResponseMessage<AppointmentResponse> getAppointmentById(Long appointmentId) {
        return null;
    }

    public ResponseMessage delete(Long appointmentId, HttpServletRequest httpServletRequest) {
        return null;
    }

    public Page<AppointmentResponse> getAllAppointmentByPage(int page, int size) {
        return null;
    }

    public ResponseMessage<AppointmentResponse> updateAppointmentById(AppointmentRequest appointmentRequest, Long appointmentId, HttpServletRequest httpServletRequest) {
        return null;
    }

    public List<AppointmentResponse> getAllAppointmentByPatient(HttpServletRequest httpServletRequest) {
        return null;
    }
}
