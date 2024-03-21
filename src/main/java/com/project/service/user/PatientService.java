package com.project.service.user;

import com.project.entity.concretes.business.TreatmentPlan;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.ChooseTreatmentPlanWithId;
import com.project.payload.request.user.PatientRequest;
import com.project.payload.request.user.PatientRequestWithoutPassword;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.PatientResponse;
import com.project.repository.user.UserRepository;
import com.project.service.UserRoleService;
import com.project.service.business.TreatmentPlanService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.DateTimeValidator;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;
    private final TreatmentPlanService treatmentPlanService;
    private final DateTimeValidator dateTimeValidator;

    public ResponseMessage<PatientResponse> savePatient(PatientRequest patientRequest) {

        //!!! unique kontrolü
        uniquePropertyValidator.checkDuplicate(patientRequest.getUsername(), patientRequest.getSsn(), patientRequest.getPhoneNumber(), patientRequest.getEmail());

        //!!! DTO -> POJO
        User patient = userMapper.mapPatientRequestToUser(patientRequest);

        //request'den gelen password'u encode ediyoruz
        patient.setPassword(passwordEncoder.encode(patientRequest.getPassword()));

        //!!! bu user'a ait setlemediğim eksik field'ları setliyoruz
        patient.setUserRole(userRoleService.getUserRole(RoleType.PATIENT));
        patient.setActive(true);
        patient.setPatientNumber(getLastNumber());

        return ResponseMessage.<PatientResponse>builder()
                .object(userMapper.mapUserToPatientResponse(userRepository.save(patient)))
                .message(SuccessMessages.PATIENT_SAVED)
                .build();
    }

    private int getLastNumber() {
        //DB de hıc patient yoksa  patient numarası olarak 1000 gonderıyoruz
        if (!userRepository.findPatient(RoleType.PATIENT)) {
            //ilk patient ise 1000 sayisini geri gonderiyoruz
            return 1000;
        }
        // DB de patient varsa son kullanılan numarayı 1 artırıp donduren method
        return userRepository.getMaxStudentNumber() + 1;
    }

    public ResponseEntity<String> updatePatient(PatientRequestWithoutPassword patientRequest, HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        User patient = userRepository.findByUsernameEquals(username);

        //!!! unique kontrol
        uniquePropertyValidator.checkUniqueProperties(patient, patientRequest);

        //!!! DTO -> POJO
        User updatedPatient = userMapper.mapPatientRequestToUpdatedUser(patientRequest);

        userRepository.save(patient);
        String message = SuccessMessages.USER_UPDATE;

        return ResponseEntity.ok(message);
    }

    public ResponseMessage<PatientResponse> updatePatientForManagers(PatientRequest patientRequest, Long userId) {

        //!!! Kullanıcıdan gelen user var mı yok mu kontrolü
        User user = methodHelper.isUserExist(userId);

        //!!! istekten gelen user in rolu patient mi ?
        methodHelper.checkRole(user, RoleType.PATIENT);

        //!!! Unique kontrolü
        uniquePropertyValidator.checkUniqueProperties(user, patientRequest);

        //!!! DTO -> POJO dönüşüm yapıyoruz
        user.setName(patientRequest.getName());
        user.setSurname(patientRequest.getSurname());
        user.setBirthDay(patientRequest.getBirthDay());
        user.setSsn(patientRequest.getSsn());
        user.setEmail(patientRequest.getEmail());
        user.setPhoneNumber(patientRequest.getPhoneNumber());
        user.setGender(patientRequest.getGender());
        user.setMotherName(patientRequest.getMotherName());
        user.setFatherName(patientRequest.getFatherName());
        user.setPassword(passwordEncoder.encode(patientRequest.getPassword()));

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_UPDATE)
                .object(userMapper.mapUserToPatientResponse(userRepository.save(user)))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage changeStatusOfPatient(Long patientId, boolean status) {
        User patient = methodHelper.isUserExist(patientId);
        methodHelper.checkRole(patient, RoleType.PATIENT);

        patient.setActive(status);
        userRepository.save(patient);

        /* !!! Önemli :

        put   -> setlemediğimiz bütün datalar null olur
        patch -> setlemediğimiz bütün datalar durumunu korur.
        Yukarıdaki mappingler tamamen semantic yapılardır. Yani kullanıcıya controller tarafında bilgi verıyoruz. Kullanıcı ön bir bilgiye sahip olur. Fakat put mapping yaparken istersek setlemediğimiz tüm verilerin nulll olmamasını da sağlayabiliriz. Yani patch mapping yaparken istersek setlemediğimiz tüm verilerin nulll olmasını da sağlayabiliriz.

        Setleme yaptığımız değişken db den geliyorsa (ki yukarı da db den geliyorsa) bu %100 patch mapping'dir.
        DB üzerinden gelen değişken üzerinden değil de (genelde update metodlarında yaptıgımız gibi) DTO -> POJO dönüşümü yaparken dönen POJO db den gelmiyor. DTO'dan dönüşen POJO üzerinde setleme yapıyoruz. Böyle bir surumda "save" dersek bütün dataları güncellememiz gerekli çünkü aksi halde put mapping gibi davrandığı için tüm verileri null'a çeker.

        */

        return ResponseMessage.builder()
                .message("Patient is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<PatientResponse> addTreatmentPlanToPatient(HttpServletRequest httpServletRequest, ChooseTreatmentPlanWithId chooseTreatmentPlanWithId) {

        // !!! username kontrolu
        String username=(String) httpServletRequest.getAttribute("username");
        User patient = methodHelper.isUserExistByUsername(username);

        //!!! Talep edilen Treatmen Planları getir
        Set<TreatmentPlan>  treatmentPlanSet=treatmentPlanService.getTreatmentPlanById(chooseTreatmentPlanWithId.getTreatmentPlanId());

        //!!! mevcuttaki Treatment Planları getir
        Set<TreatmentPlan> patientCurrentTreatmentPlan= patient.getTreatmentPlanList();

        //!!! talep edilen ile mevcutta olan arasında bir çakışma var mı?
        dateTimeValidator.checkTreatmentPlans(patientCurrentTreatmentPlan,treatmentPlanSet);

        patientCurrentTreatmentPlan.addAll(treatmentPlanSet);
        patient.setTreatmentPlanList(patientCurrentTreatmentPlan);

        User savedPatient = userRepository.save(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.TREATMENT_PLAN_ADD_TO_PATIENT)
                .object(userMapper.mapUserToPatientResponse(savedPatient))
                .httpStatus(HttpStatus.OK)
                .build();


    }
}


