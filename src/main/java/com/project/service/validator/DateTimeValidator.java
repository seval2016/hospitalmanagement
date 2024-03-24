package com.project.service.validator;

import com.project.entity.concretes.business.TreatmentPlan;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {
    private boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop) ;
    }

    public void checkDateWithException(LocalTime start, LocalTime stop) {
        if(checkTime(start, stop)){
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

  //!!! Talep edilen yeni treatment planlar arasinda cakisma var mi kontrolu
    private void checkDuplicateTreatmentPlans(Set<TreatmentPlan> treatmentPlans) {
        Set<String> uniqueTreatmentPlanDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for (TreatmentPlan treatmentPlan : treatmentPlans) {
            String treatmentPlanDay = treatmentPlan.getDay().name();

            // !!! Karsilastirilan treatment planlar ayni gunde ise
            if (uniqueTreatmentPlanDays.contains(treatmentPlanDay)) {

                //!!! Baslama saatine gore kontrol
                for (LocalTime startTime : existingLessonProgramStartTimes) {

                    //!!! Baslama saatleri esit ise
                    if (treatmentPlan.getStartTime().equals(startTime)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }

                    // !!! mevcut treatment Planın başlangıç saati ile diğer bir treatment Planın başlangıç ve bitiş saatleri arasında çakışma olduğunda hata fırlatır.
                    if (treatmentPlan.getStartTime().isBefore(startTime)&& treatmentPlan.getStopTime().isAfter(startTime)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }
                }
                //!!! Bitis saatine gore kontrol
                for(LocalTime stopTime : existingLessonProgramStopTimes){
                        if(treatmentPlan.getStartTime().isBefore(stopTime) && treatmentPlan.getStopTime().isAfter(stopTime)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }
                }
            }
            //!!! yukardaki Baslangic ve Bitis saatine gore kontrollerden gecen TreatmenPlan, metodun en basinda olusturulan unique degiskenlere ataniyor

            uniqueTreatmentPlanDays.add(treatmentPlanDay);
            existingLessonProgramStartTimes.add(treatmentPlan.getStartTime());
            existingLessonProgramStopTimes.add(treatmentPlan.getStopTime());

        }

    }

    //!!!  mevcut treatment plan ile talep edilen treatment planları arasında karşılaştırma

    private void checkDuplicateTreatmentPlans(Set<TreatmentPlan> existTreatmentPlan,
                                              Set<TreatmentPlan> treatmentPlanRequest) {
        for (TreatmentPlan requestTreatmentPlan : treatmentPlanRequest) {
            String requestTreatmentPlanDay = requestTreatmentPlan.getDay().name();
            LocalTime requestStart = requestTreatmentPlan.getStartTime();
            LocalTime requestStop = requestTreatmentPlan.getStopTime();

            if (existTreatmentPlan.stream()
                    .anyMatch(treatmentPlan ->
                            treatmentPlan.getDay().name().equals(requestTreatmentPlanDay)
                                    && (treatmentPlan.getStartTime().equals(requestStart)
                                    || (treatmentPlan.getStartTime().isBefore(requestStart) && treatmentPlan.getStopTime().isAfter(requestStart))
                                    || (treatmentPlan.getStartTime().isBefore(requestStop) && treatmentPlan.getStopTime().isBefore(requestStop))
                                    || (treatmentPlan.getStartTime().isAfter(requestStart) && treatmentPlan.getStopTime().isBefore(requestStop))))
            ) {
                throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
            }
        }
    }

    public void checkTreatmentPlans(Set<TreatmentPlan> existTreatmentPlan,
                                    Set<TreatmentPlan> treatmentPlanRequest) {
        // !!! mevcut treatment plan bos ise ve requestten gelen treatment plan doluysa
        if (existTreatmentPlan.isEmpty() && treatmentPlanRequest.size() > 1) {
            checkDuplicateTreatmentPlans(treatmentPlanRequest);
        } else {
            // !!! talep edilen icinde cakisma var mi
            checkDuplicateTreatmentPlans(treatmentPlanRequest);

            // !!! talep edilen ile mevcutta cakisma var mi
            checkDuplicateTreatmentPlans(existTreatmentPlan, treatmentPlanRequest);

        }
    }



}
