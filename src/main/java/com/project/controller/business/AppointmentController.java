package com.project.controller.business;

import com.project.payload.request.business.AppointmentRequest;
import com.project.payload.response.business.AppointmentResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/save") // hhtp://localhost:8080/appointment/save + JSON + POST
    @PreAuthorize("hasAnyAuthority('DOCTOR')")
    public ResponseMessage<AppointmentResponse> saveAppointment(HttpServletRequest httpServletRequest,
                                                         @RequestBody @Valid AppointmentRequest appointmentRequest){
        return appointmentService.saveAppointment(httpServletRequest, appointmentRequest);
    }

    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getAll")  // http://localhost:8080/appointment/getAll
    public List<AppointmentResponse> getAll(){
        return appointmentService.getAll();
    }

    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getAppointmentById/{appointmentId}")  // http://localhost:8080/appointment/getAppointmentById/1
    public ResponseMessage<AppointmentResponse> getAppointmentById(@PathVariable Long appointmentId){
        return appointmentService.getAppointmentById(appointmentId);
    }

    @DeleteMapping("/delete/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')") // http://localhost:8080/appointment/delete/1
    public ResponseMessage delete(@PathVariable Long appointmentId, HttpServletRequest httpServletRequest){
        return appointmentService.delete(appointmentId, httpServletRequest);
    }

    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getAllAppointmentByPage") //http://localhost:8080/appointment/getAllAppointmentByPage?page=0&size=1
    public Page<AppointmentResponse> getAllAppointmentByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return appointmentService.getAllAppointmentByPage(page,size);
    }

    @PutMapping("/update/{appointmentId}") //http://localhost:8080/appointment/update/1
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    public ResponseMessage<AppointmentResponse> updateAppointmentById(@RequestBody @Valid AppointmentRequest appointmentRequest,
                                                        @PathVariable Long appointmentId,
                                                        HttpServletRequest httpServletRequest) {
        return appointmentService.updateAppointmentById(appointmentRequest, appointmentId, httpServletRequest);
    }

    @GetMapping("/getAllAppointmentByPatient")  //http://localhost:8080/appointment/getAllAppointmentByPatient
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public List<AppointmentResponse> getAllAppointmentByPatient(HttpServletRequest httpServletRequest){
        return appointmentService.getAllAppointmentByPatient(httpServletRequest);
    }

}
