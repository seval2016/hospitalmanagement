package com.project.service.business;

import com.project.entity.concretes.business.Appointment;
import com.project.entity.concretes.user.User;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.AppointmentMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.AppointmentRequest;
import com.project.payload.response.business.AppointmentResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.AppointmentRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.user.DoctorService;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.project.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final UserService userService;
    private final PageableHelper pageableHelper;
    private final AppointmentMapper appointmentMapper;
    private final DoctorService doctorService;


    public ResponseMessage<AppointmentResponse> saveAppointment(HttpServletRequest httpServletRequest, AppointmentRequest appointmentRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        User doctorId = methodHelper.isUserExistByUsername(username);

        //!!! Doktor'un mevcut appointment'leri ile cakisma var mi
        dateTimeValidator.checkDateWithException(appointmentRequest.getStartTime(), appointmentRequest.getStopTime());

        checkAppointmentConflict(doctorId.getId(), appointmentRequest.getDate(), appointmentRequest.getStartTime(), appointmentRequest.getStopTime());

        //!!! Appointment'e katilacak Patient getiriliyor
        User patients =userService.getPatientById(appointmentRequest.getPatientId());

        //!!! DTO -> POJO
        Appointment appointment=appointmentMapper.mapAppointmentRequestToAppointment(appointmentRequest);
        appointment.setPatient(patients);

        Appointment savedAppointment=appointmentRepository.save(appointment);

        return ResponseMessage.<AppointmentResponse>builder()
                .message(SuccessMessages.APPOINTMENT_SAVED)
                .object(appointmentMapper.mapAppointmentToAppointmentResponse(savedAppointment))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    private void checkAppointmentConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime stoptime) {

        List<Appointment> appointments;

        //!!! Patient veya Doctor ait olan mevcut Appointmentler getiriliyor.
        if (Boolean.TRUE.equals(userService.getUserByUserId(userId))) {
            appointments = appointmentRepository.getByDoctor_IdEquals(userId);
        } else appointments = appointmentRepository.findByPatientList_IdEquals(userId);

        //!!! cakisma kontrolu
        for (Appointment appointment : appointments) {
            LocalTime existingStartTime = appointment.getStartTime();
            LocalTime existingStopTime = appointment.getStopTime();

            if (appointment.getDate().equals(date) &&
                    (
                            (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                                    (stoptime.isAfter(existingStartTime) && stoptime.isBefore(existingStopTime)) ||
                                    (startTime.isBefore(existingStartTime) && stoptime.isAfter(existingStopTime)) ||
                                    (startTime.equals(existingStartTime) || stoptime.equals(existingStopTime))
                    )
            ) {
                throw new ConflictException(ErrorMessages.APPOINTMENT_HOURS_CONFLICT);
            }

        }
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::mapAppointmentToAppointmentResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<AppointmentResponse> getAppointmentById(Long appointmentId) {
        return ResponseMessage.<AppointmentResponse>builder()
                .message(SuccessMessages.APPOINTMENT_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(appointmentMapper.mapAppointmentToAppointmentResponse(isAppointmentExistById(appointmentId)))
                .build();
    }

    private Appointment isAppointmentExistById(Long appointmentId){
        return appointmentRepository
                .findById(appointmentId).orElseThrow(
                        ()->new ResourceNotFoundException(String.format(ErrorMessages.APPOINTMENT_NOT_FOUND_MESSAGE,appointmentId)));

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
