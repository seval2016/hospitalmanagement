package com.project.service.business;

import com.project.entity.concretes.business.Appointment;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
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
import org.springframework.data.domain.Pageable;
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


    public ResponseMessage<AppointmentResponse> saveAppointment(HttpServletRequest httpServletRequest, AppointmentRequest appointmentRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        User doctor = methodHelper.isUserExistByUsername(username);

        //!!! Doktor'un mevcut appointment'leri ile cakisma var mi
        dateTimeValidator.checkDateWithException(appointmentRequest.getStartTime(), appointmentRequest.getStopTime());

        checkAppointmentConflict(doctor.getId(), appointmentRequest.getDate(), appointmentRequest.getStartTime(), appointmentRequest.getStopTime());

        //!!! Appointment'e katilacak Patient getiriliyor
        User patient = userService.getPatientById(appointmentRequest.getPatientId());

        //!!! DTO -> POJO
        Appointment appointment = appointmentMapper.mapAppointmentRequestToAppointment(appointmentRequest);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return ResponseMessage.<AppointmentResponse>builder()
                .message(SuccessMessages.APPOINTMENT_SAVED)
                .object(appointmentMapper.mapAppointmentToAppointmentResponse(savedAppointment))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    //Long userId 'ı parametre olarak almamızın sebebi : Gönderdiğimiz userId eğer bir doctorId ise o doctor üzerinden conflict var mı yada öğrenci üzerinden geliyor ise öğrenci üzerinden çakışma var mı diye kontrol edecek.
    private void checkAppointmentConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime stoptime) {

        List<Appointment> appointments;

        //!!! Patient veya Doctor ait olan mevcut Appointmentler getiriliyor.
        if (Boolean.TRUE.equals(userService.getUserByUserId(userId).getPatientDoctorId())) {
            appointments = appointmentRepository.getByDoctor_IdEquals(userId); //appointment tablosuna gider id'si verilen doktor'un randevularını getirir.
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

    private Appointment isAppointmentExistById(Long appointmentId) {
        return appointmentRepository
                .findById(appointmentId).orElseThrow(
                        () -> new ResourceNotFoundException(String.format(ErrorMessages.APPOINTMENT_NOT_FOUND_MESSAGE, appointmentId)));

    }

    public ResponseMessage delete(Long appointmentId, HttpServletRequest httpServletRequest) {
        Appointment appointment = isAppointmentExistById(appointmentId);

        isDoctorControl(appointment, httpServletRequest);
        appointmentRepository.deleteById(appointmentId);
        return ResponseMessage.builder()
                .message(SuccessMessages.APPOINTMENT_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private void isDoctorControl(Appointment appointment, HttpServletRequest httpServletRequest) {
        //!!! Doctor ise sadece kendi randevularını silebilsin
        String userName = (String) httpServletRequest.getAttribute("username");
        User doctor = methodHelper.isUserExistByUsername(userName);
        if (
                (doctor.getUserRole().getRoleType().equals(RoleType.DOCTOR)) && // metodu tetikleyenin Role bilgisi DOKTOR ise
                        !(appointment.getDoctor().getId().equals(doctor.getId())) // DOCTOR, baskasinin Appointment ini silmeye calisiyorsa
        ) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    public Page<AppointmentResponse> getAllAppointmentByPage(int page, int size) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        return appointmentRepository.findAll(pageable).map(appointmentMapper::mapAppointmentToAppointmentResponse);
    }

    public ResponseMessage<AppointmentResponse> updateAppointmentById(AppointmentRequest appointmentRequest, Long appointmentId, HttpServletRequest httpServletRequest) {
        Appointment appointment = isAppointmentExistById(appointmentId);

        isDoctorControl(appointment, httpServletRequest);

        dateTimeValidator.checkDateWithException(appointmentRequest.getStartTime(), appointmentRequest.getStopTime());
        if (!(
                appointment.getDate().equals(appointmentRequest.getDate()) &&
                        appointment.getStartTime().equals(appointmentRequest.getStartTime()) &&
                        appointment.getStopTime().equals(appointmentRequest.getStopTime())
        )
        ) {

            checkAppointmentConflict(appointmentRequest.getPatientId(), appointmentRequest.getDate(), appointmentRequest.getStartTime(), appointmentRequest.getStopTime());

            checkAppointmentConflict(appointment.getDoctor().getId(), appointmentRequest.getDate(), appointmentRequest.getStartTime(), appointmentRequest.getStopTime());
        }

        User patients = userService.getPatientById(appointmentRequest.getPatientId());

        //!!! DTO --> POJO
        Appointment updatedAppointment = appointmentMapper.mapAppointmentUpdateRequestToAppointment(appointmentRequest, appointmentId);
        updatedAppointment.setPatient(patients);

        Appointment savedAppointment = appointmentRepository.save(updatedAppointment);

        return ResponseMessage.<AppointmentResponse>builder()
                .message(SuccessMessages.APPOINTMENT_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(appointmentMapper.mapAppointmentToAppointmentResponse(savedAppointment))
                .build();
    }

    public List<AppointmentResponse> getAllAppointmentByDoctor(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        User doctor = userService.getDoctorByUsername(username);
        methodHelper.checkRole(doctor, RoleType.DOCTOR);

        return appointmentRepository.getByDoctor_IdEquals(doctor.getId())
                .stream()
                .map(appointmentMapper::mapAppointmentToAppointmentResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAllAppointmentByPatient(HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");
        User patient = methodHelper.isUserExistByUsername(userName);

        methodHelper.checkRole(patient, RoleType.PATIENT);

        return appointmentRepository.findByPatientList_IdEquals(patient.getId())
                .stream()
                .map(appointmentMapper::mapAppointmentToAppointmentResponse)
                .collect(Collectors.toList());

    }
}
