package com.project.payload.mappers;

import com.project.entity.concretes.user.User;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.request.user.UserRequestWithoutPassword;
import com.project.payload.response.user.DoctorResponse;
import com.project.payload.response.user.PatientResponse;
import com.project.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //User'ı UserResponse'a çevireceğiz. Yani POJO ---> DTO çevirilecek
    public UserResponse mapUserToUserResponse(User user){
        return  UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();
    }

    public User mapUserRequestToUser(BaseUserRequest userRequest){

        return User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .built_in(userRequest.getBuiltIn())
                .build();
    }

    public PatientResponse mapUserToPatientResponse(User patient){

        return PatientResponse.builder()
                .userId(patient.getId())
                .username(patient.getUsername())
                .name(patient.getName())
                .surname(patient.getSurname())
                .birthDay(patient.getBirthDay())
                .birthPlace(patient.getBirthPlace())
                .phoneNumber(patient.getPhoneNumber())
                .gender(patient.getGender())
                .email(patient.getEmail())
                .fatherName(patient.getFatherName())
                .motherName(patient.getMotherName())
                .patientNumber(patient.getPatientNumber())
                .isActive(patient.isActive())
                .build();
    }

    public DoctorResponse mapUserToDoctorResponse(User doctor){

        return DoctorResponse.builder()
                .userId(doctor.getId())
                .username(doctor.getUsername())
                .name(doctor.getName())
                .surname(doctor.getSurname())
                .birthDay(doctor.getBirthDay())
                .birthPlace(doctor.getBirthPlace())
                .ssn(doctor.getSsn())
                .phoneNumber(doctor.getPhoneNumber())
                .gender(doctor.getGender())
                .email(doctor.getEmail())
                .treatmentPlans(doctor.getTreatmentPlanList())
                .isChiefDoctor(doctor.getIsChiefDoctor())
                .build();
    }

    public User mapUserRequestToUpdatedUser(UserRequest userRequest, Long userId){

        return User.builder()
                .id(userId)
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .build();

    }

}