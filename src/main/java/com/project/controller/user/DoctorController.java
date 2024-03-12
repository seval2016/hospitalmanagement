package com.project.controller.user;

import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.payload.response.user.PatientResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private DoctorService doctorService;

    @PostMapping("/save") //http://localhost:8080/doctor/save
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@RequestBody @Valid DoctorRequest doctorRequest) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctorRequest));
    }

    //Yöneticilerin bir doktoru güncellediği method
    @PutMapping("/update/{userId}")//http://localhost:8080/doctor/update/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<DoctorResponse> updateTeacherForManagers(@RequestBody @Valid DoctorRequest doctorRequest, @PathVariable Long userId) {

        return doctorService.updateDoctorForManagers(doctorRequest, userId);
    }

    //Bir doctorun sisteme girip kendi hastalarını getirdiği method

    @GetMapping("/getAllPatientByUsername")
    @PreAuthorize("hasAnyAuthority('DOCTOR')")//http://localhost:8080/doctor/getAllPatientByUsername
    public List<PatientResponse> getAllPatientByUsername(HttpServletRequest request) {
        String userName = request.getHeader("username");
        return doctorService.getAllPatientByUsername(userName);
    }

    //Yöneticiler tarafından bir başhekim (Chief Doctor) atama

    @PatchMapping("/saveChiefDoctor/{doktorId}")//http://localhost:8080/doctor/saveChiefDoctor/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<DoctorResponse> saveChiefDoctor(@PathVariable Long doctorId){
            return doctorService.saveChiefDoctor(doctorId);
    }

    @DeleteMapping("/deleteChiefDoctorById/{doktorId}")//http://localhost:8080/doctor/deleteChiefDoctorById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    //Normalde doctor entity'si silinmiyor entity içerisindeki bir değer değişiyor bu yüzden UserResponsedöndürüyoruz
    public ResponseMessage<UserResponse> deleteChiefDoctorById(@PathVariable Long chiefDoctorId){
        return doctorService.deleteChiefDoctorById(chiefDoctorId);

    }


}
