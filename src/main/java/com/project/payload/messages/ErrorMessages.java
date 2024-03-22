package com.project.payload.messages;

public class ErrorMessages {


    private ErrorMessages() { //bu classdan hiçbir şekilde nesne oluşturulmasın diye yapıldı
    }
    public static final String NOT_PERMITTED_METHOD_MESSAGE = "You do not have any permission to do this operation";
    public static final String PASSWORD_NOT_MATCHED = "Your passwords are not matched";
    public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error : User with username %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_SSN = "Error : User with ssn %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error : User with email %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_PHONE = "Error : User with phone number %s is already registered";
    public static final String ROLE_NOT_FOUND = "There is no role like that, check the database";
    public static final String NOT_FOUND_USER_USERROLE_MESSAGE = "Error: User not found with user-role %s";

    public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with id %s";

    public static final String NOT_FOUND_USER_WITH_ROLE_MESSAGE = "Error: The role information of the user with id %s is not role: %s";

    public static final String NOT_FOUND_CHIEFDOCTOR_MESSAGE = "Error: Chief Doctor with id %s not found";
    public static final String ALREADY_EXIST_CHIEFDOCTOR_MESSAGE ="Chief Doctor with id %s is already exist";

    public static final String MEDICAL_RECORD_START_DATE_IS_EARLIER_THAN_LAST_RECORD_DATE = "Error: The start date cannot be earlier than the record date";
    public static final String MEDICAL_RECORD_END_DATE_IS_EARLIER_THAN_START_DATE = "Error: The end date cannot be earlier than start date";
    public static final String MEDICAL_RECORD_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE = "ERROR : Medical Record with Term and Year is already exist";
    public static final String MEDICAL_RECORD_CONFLICT_MESSAGE ="Error: There is a conflict regarding the dates of the medical records";
    public static final String MEDICAL_RECORD_NOT_FOUND_MESSAGE ="Error: Medical Record with id %s not found";


    public static final String DEPARTMENT_ALREADY_EXIST_WITH_DEPARTMENT_NAME = "DEPARTMENT with Department Name %s is already exist";
    public static final String NOT_FOUND_DEPARTMENT_MESSAGE = "Error: Department with this field %s is not found";

    public static final String NOT_FOUND_DEPARTMENT_IN_LIST = "Error: Department not found in the list";
    public static final String TIME_NOT_VALID_MESSAGE = "Error: incorrect time";


    public static final String NOT_FOUND_TREATMENT_PLAN_MESSAGE = "ERROR : Treatment Plan with this field %s not found";
    public static final String NOT_FOUND_TREATMENT_PLAN_MESSAGE_WITHOUT_ID_INFO="Error: Treatment Plan with this field not found";
    public static final String TREATMENT_PLAN_ALREADY_EXIST ="Error: Course schedule can not be selected for the same hour and date";


    public static final String PATIENT_INFO_NOT_FOUND = "Error: Patient Info with id %s not found";
    public static final String PATIENT_INFO_NOT_FOUND_BY_PATIENT_ID= "Error: Patient Info with  patient id %d not found" ;



    public static final String APPOINTMENT_HOURS_CONFLICT = "Error: Appointment hours has conflict with existing appointments";
    public static final String APPOINTMENT_NOT_FOUND_MESSAGE = "Error: Appointment with id %d not found" ;


}
