package com.project.controller.business;

import com.project.service.business.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/save") // hhtp://localhost:8080/appointment/save + JSON + POST
    @PreAuthorize("hasAnyAuthority('DOCTOR')")
    public ResponseMessage<AppointmentResponse> saveMeet(HttpServletRequest httpServletRequest,
                                                  @RequestBody @Valid AppointmentRequest meetRequest){
        return appointmentService.saveAppointment(httpServletRequest, meetRequest);
    }
}
