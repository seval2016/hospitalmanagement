package com.project.service.user;

import com.project.entity.concretes.business.TreatmentPlan;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.ConflictException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.payload.response.user.PatientResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;

import com.project.service.UserRoleService;
import com.project.service.business.TreatmentPlanService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;
    private final TreatmentPlanService treatmentPlanService;

    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest doctorRequest) {

        // !!! ThreatmentPlan getirme işlemi yapılıyor
       Set<TreatmentPlan> treatmentPlanSet= treatmentPlanService.getTreatmentPlanById(doctorRequest.getTreatmentPlanIdList());

        //!!! unique kontrolü
        uniquePropertyValidator.checkDuplicate(doctorRequest.getUsername(), doctorRequest.getSsn(), doctorRequest.getPhoneNumber(), doctorRequest.getEmail());

        //!!! DTO -> POJO
        User doctor = userMapper.doctorRequestToUser(doctorRequest);


        //!!! pojo class da olması gerekipte dto da olmayan field'ları service katında setliyoruz.
        // Bundan dolayı bu kısımda da dto da olmayan role bilgisini setliyoruz
        doctor.setUserRole(userRoleService.getUserRole(RoleType.DOCTOR));

        //ThreatmentPlan'den gelen verileri setleme işlemi yapılacak
        doctor.setTreatmentPlanList(treatmentPlanSet);

        //pasword encode etme
        doctor.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));

        //isChefDoctor kontrolü

        if (doctorRequest.getIsChiefDoctor()) {
            doctor.setIsChiefDoctor(Boolean.TRUE);
        } else doctor.setIsChiefDoctor(Boolean.FALSE);

        User savedDoctor = userRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_SAVED)
                .httpStatus(HttpStatus.CREATED)
                .object(userMapper.mapUserToDoctorResponse(savedDoctor))
                .build();
    }

    public ResponseMessage<DoctorResponse> updateDoctorForManagers(DoctorRequest doctorRequest, Long userId) {

        //!!! aranan user var mı yok mu yani id kontrolu
        User user = methodHelper.isUserExist(userId);

        //!!! Parametre de gelen user rolü (endpointi tetiklemesi gerekn kişi) Doctor mu ?
        methodHelper.checkRole(user, RoleType.DOCTOR);

        // !!! ThreatmentPlan getirme işlemi yapılıyor
        Set<TreatmentPlan> treatmentPlanSet= treatmentPlanService.getTreatmentPlanById(doctorRequest.getTreatmentPlanIdList());

        //!!! Unique kontrolü yapılıyor
        uniquePropertyValidator.checkUniqueProperties(user, doctorRequest);

        //!!! DTO -> POJO
        User updatedDoctor = userMapper.mapDoctorRequestToUpdatedUser(doctorRequest, userId);

        //!!! password encode etme
        updatedDoctor.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));

        //!!! treatment plan setlenıyor
        updatedDoctor.setTreatmentPlanList(treatmentPlanSet);

        //!!! PutMapping yaptıgımız için Rol Bilgisini setliyoruz
        updatedDoctor.setUserRole(userRoleService.getUserRole(RoleType.DOCTOR));

        User savedDoctor = userRepository.save(updatedDoctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToDoctorResponse(savedDoctor))
                .build();
    }

    public List<PatientResponse> getAllPatientByUsername(String userName) {

        //!!! Gelen username db'de var mı ?
        User doctor = methodHelper.isUserExistByUsername(userName);

        //!!! İstek yapan user Chief Doctor mu ?
         methodHelper.checkChief(doctor);

         //!!! doctor'un hastalarına ulaşmamız gerekiyor

        return userRepository.findByPatientDoctorId(doctor.getId())
                .stream()
                .map(userMapper::mapUserToPatientResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<DoctorResponse> saveChiefDoctor(Long doctorId) {
        //!!! aranan user var mı yok mu yani id kontrolu
        User doctor = methodHelper.isUserExist(doctorId);

        //!!! Parametre de gelen user rolü (endpointi tetiklemesi gereken kişi) Doctor mu ?
        methodHelper.checkRole(doctor, RoleType.DOCTOR);

        //!!! Bu doctor zaten chief doctor ise tekrar yapmamıza izin vermez
        if(Boolean.TRUE.equals(doctor.getIsChiefDoctor())){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_EXIST_CHIEFDOCTOR_MESSAGE,doctorId));
        }

        doctor.setIsChiefDoctor(Boolean.TRUE);
        User savedDoctor=userRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.CHIEF_DOCTOR_SAVE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToDoctorResponse(savedDoctor))//bir sorun yaşarsan 11. video ya bak
                .build();

    }

    public ResponseMessage<UserResponse> deleteChiefDoctorById(Long chiefDoctorId) { //bir sorun yaşarsan 11. video ya bak
        //!!! aranan user var mı yok mu yani id kontrolu
        User doctor = methodHelper.isUserExist(chiefDoctorId);

        //!!! Parametre de gelen user rolü (endpointi tetiklemesi gereken kişi) Doctor mu ?
        methodHelper.checkRole(doctor, RoleType.DOCTOR);

        //!!! Bulunan bu user Chief Doctor mu ?
        methodHelper.checkChief(doctor);

        doctor.setIsChiefDoctor(Boolean.FALSE);

        userRepository.save(doctor);

        //silinen başhekimin hastaları varsa bu ilişkiyi koparmak lazım
        List<User> allPatients=userRepository.findByPatientDoctorId(chiefDoctorId);

        if(allPatients.isEmpty()){
            allPatients.forEach(patient -> patient.setIsChiefDoctor(null));
        }

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.CHIEF_DOCTOR_DELETE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(doctor))
                .build();
    }

    //Tüm doktorlar
    public List<UserResponse> getAllDoctor() {
        return userRepository.findAllDoctor()
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());

    }
}
