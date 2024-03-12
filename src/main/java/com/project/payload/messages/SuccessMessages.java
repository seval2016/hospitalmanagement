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

    public static final String EDUCATION_TERM_SAVED = "Education Term is Saved Successfully";
    public static final String EDUCATION_TERM_DELETE = "Education Term is Deleted Successfully";
    public static final String EDUCATION_TERM_UPDATE = "Education Term is updated successfully ";

    public static final String LESSON_SAVED = "Lesson is Saved Successfully";
    public static final String LESSON_DELETE = "Lesson is Deleted Successfully";
    public static final String LESSON_FOUND = "Lesson is Found Successfully";

    public static final String LESSON_PROGRAM_SAVED = "Lesson Program is Saved Successfully";
    public static final String LESSON_PROGRAM_DELETE = "Lesson Program is Deleted Successfully";
    public static final String LESSON_PROGRAM_ADD_TO_PATIENT = "Lesson Program added to student";
    public static final String LESSON_PROGRAM_ADD_TO_DOCTOR = "Lesson Program added to doctor";


    public static final String PATIENT_INFO_SAVED = "Patient Info is Saved Successfully";
    public static final String PATIENT_INFO_DELETE = "Patient Info is Deleted Successfully";
    public static final String PATIENT_INFO_UPDATE = "Patient Info is Updated Successfully";


    public static final String MEET_SAVED = "Meet is Saved Successfully";
    public static final String MEET_FOUND = "Meet is Found Successfully";
    public static final String MEET_DELETE = "Meet is Deleted Successfully";
    public static final String MEET_UPDATE = "Meet is Updated Successfully";
}
