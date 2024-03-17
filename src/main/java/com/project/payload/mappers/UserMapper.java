package com.project.payload.mappers;

import com.project.entity.concretes.user.User;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.request.user.*;
import com.project.payload.response.user.DoctorResponse;
import com.project.payload.response.user.PatientResponse;
import com.project.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //User'ı UserResponse'a çevireceğiz. Yani POJO ---> DTO çevirilecek
    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
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

    public User mapUserRequestToUser(BaseUserRequest userRequest) {
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

    public PatientResponse mapUserToPatientResponse(User patient) {

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

    public DoctorResponse mapUserToDoctorResponse(User doctor) {

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

    public User mapUserRequestToUpdatedUser(UserRequest userRequest, Long userId) {

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

    public User doctorRequestToUser(DoctorRequest doctorRequest) {
        return User.builder()
                .name(doctorRequest.getName())
                .surname(doctorRequest.getSurname())
                .ssn(doctorRequest.getSsn())
                .username(doctorRequest.getUsername())
                .birthDay(doctorRequest.getBirthDay())
                .birthPlace(doctorRequest.getBirthPlace())
                .password(doctorRequest.getPassword())
                .phoneNumber(doctorRequest.getPhoneNumber())
                .email(doctorRequest.getEmail())
                .isChiefDoctor(doctorRequest.getIsChiefDoctor())
                .built_in(doctorRequest.getBuiltIn())
                .gender(doctorRequest.getGender())
                .build();
    }

    public User mapDoctorRequestToUpdatedUser(DoctorRequest doctorRequest, Long userId) {

        return User.builder()
                .id(userId)
                .username(doctorRequest.getUsername())
                .name(doctorRequest.getName())
                .surname(doctorRequest.getSurname())
                .password(doctorRequest.getPassword())
                .ssn(doctorRequest.getSsn())
                .birthDay(doctorRequest.getBirthDay())
                .birthPlace(doctorRequest.getBirthPlace())
                .phoneNumber(doctorRequest.getPhoneNumber())
                .gender(doctorRequest.getGender())
                .email(doctorRequest.getEmail())
                .isChiefDoctor(doctorRequest.getIsChiefDoctor())
                .built_in(doctorRequest.getBuiltIn())
                .build();
    }

    public User mapPatientRequestToUser(PatientRequest patientRequest) {
        return User.builder()
                .fatherName(patientRequest.getFatherName())
                .motherName(patientRequest.getMotherName())
                .birthDay(patientRequest.getBirthDay())
                .birthPlace(patientRequest.getBirthPlace())
                .name(patientRequest.getName())
                .surname(patientRequest.getSurname())
                .password(patientRequest.getPassword())
                .username(patientRequest.getUsername())
                .ssn(patientRequest.getSsn())
                .email(patientRequest.getEmail())
                .phoneNumber(patientRequest.getPhoneNumber())
                .gender(patientRequest.getGender())
                .built_in(patientRequest.getBuiltIn())
                .build();
    }


    public User mapPatientRequestToUpdatedUser( PatientRequestWithoutPassword patientRequest) {
        return User.builder()
                .motherName(patientRequest.getMotherName())
                .fatherName(patientRequest.getFatherName())
                .birthDay(patientRequest.getBirthDay())
                .birthPlace(patientRequest.getBirthPlace())
                .gender(patientRequest.getGender())
                .name(patientRequest.getName())
                .surname(patientRequest.getSurname())
                .ssn(patientRequest.getSsn())
                .build();

    }
}