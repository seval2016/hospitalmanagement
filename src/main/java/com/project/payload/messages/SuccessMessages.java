package com.project.payload.messages;

public class SuccessMessages {




    private SuccessMessages() { //bu classdan hiçbir şekilde nesne oluşturulmasın diye yapıldı
    }

    public static final String PASSWORD_CHANGED_RESPONSE_MESSAGE = "Password Successfully Changed";

    public static final String USER_CREATED = "User is Saved Successfully";
    public static final String USER_FOUND = "User is Found Successfully";
    public static final String USER_DELETE = "User is Deleted Successfully";
    public static final String USER_UPDATE = "Your information has been updated successfully ";
    public static final String USER_UPDATE_MESSAGE = "User is Updated Successfully";

    public static final String DOCTOR_SAVED ="Doctor is saved Successfully" ;
    public static final String DOCTOR_UPDATE = "Doctor is updated successfully ";

    public static final String CHIEF_DOCTOR_SAVE = "Chief Doctor is Saved Successfully";
    public static final String CHIEF_DOCTOR_DELETE = "Chief Doctor is Deleted Successfully";

    public static final String PATIENT_SAVED = "Patient is Saved Successfully";
    public static final String PATIENT_UPDATE = "Patient is updated successfully ";

    public static final String MEDICAL_RECORD_SAVED = "Medical Record is Saved Successfully";
    public static final String MEDICAL_RECORD_DELETE = "Medical Record is Deleted Successfully";
    public static final String MEDICAL_RECORD_UPDATE = "Medical Record is updated successfully ";

    public static final String DEPARTMENT_SAVED = "Department is Saved Successfully";
    public static final String DEPARTMENT_DELETE = "Department is Deleted Successfully";
    public static final String DEPARTMENT_FOUND = "Department is Found Successfully";

    public static final String TREATMENT_PLAN_SAVED = "Treatment Plan is Saved Successfully";
    public static final String TREATMENT_PLAN_DELETE = "Treatment Plan is Deleted Successfully";
    public static final String TREATMENT_PLAN_ADD_TO_PATIENT = "Treatment Plan added to student";
    public static final String TREATMENT_PLAN_ADD_TO_DOCTOR = "Treatment Plan added to doctor";


    public static final String PATIENT_INFO_SAVED = "Patient Info is Saved Successfully";
    public static final String PATIENT_INFO_DELETE = "Patient Info is Deleted Successfully";
    public static final String PATIENT_INFO_UPDATE = "Patient Info is Updated Successfully";


    public static final String APPOINTMENT_SAVED = "Appointment is Saved Successfully";
    public static final String APPOINTMENT_FOUND = "Appointment is Found Successfully";
    public static final String APPOINTMENT_DELETE = "Appointment is Deleted Successfully";
    public static final String APPOINTMENT_UPDATE = "Appointment is Updated Successfully";
}
