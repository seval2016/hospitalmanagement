package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.repository.user.UserRepository;

import com.project.service.UserRoleService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;

    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest doctorRequest) {

       // !!! TODO : ThreatmentPlan Kontrolü yapılacak

        //!!! unique kontrolü
        uniquePropertyValidator.checkDuplicate(doctorRequest.getUsername(),doctorRequest.getSsn(),doctorRequest.getPhoneNumber(),doctorRequest.getEmail());

        //!!! DTO -> POJO
       User doctor= userMapper.doctorRequestToUser(doctorRequest);


        //!!! pojo class da olması gerekipte dto da olmayan field'ları service katında setliyoruz.
        // Bundan dolayı bu kısımda da dto da olmayan role bilgisini setliyoruz
           doctor.setUserRole(userRoleService.getUserRole(RoleType.DOCTOR));

           //TODO: ThreatmentPlan setleme işlemi yapılacak

        //pasword encode etme
        doctor.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));

        //isChefDoctor kontrolü

        if(doctorRequest.getIsChiefDoctor()){
            doctor.setIsChiefDoctor(Boolean.TRUE);
        }else doctor.setIsChiefDoctor(Boolean.FALSE);

        User savedDoctor=userRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_SAVED)
                .httpStatus(HttpStatus.CREATED)
                .object(userMapper.mapUserToDoctorResponse(savedDoctor))
                .build();
    }

    public ResponseMessage<DoctorResponse> updateTeacherForManagers(DoctorRequest doctorRequest, Long userId) {

        //!!! id kontrolu
        User user = methodHelper.isUserExist(userId);

        //!!! parametrede gelen User gercekten Doctor mu kontrolu
        methodHelper.checkRole(user, RoleType.DOCTOR);

    }
}
