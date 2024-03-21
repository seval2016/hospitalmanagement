package com.project.service.validator;

import com.project.entity.concretes.business.TreatmentPlan;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {
    private boolean checkDate(LocalDateTime start, LocalDateTime stop) {
        return start.isAfter(stop) || start.equals(stop);
    }

    public void checkDateWithException(LocalDateTime start, LocalDateTime stop) {
        if (checkDate(start, stop)) {
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

    //!!! Talep edilen, yeni treatment planlar arasinda cakisma var mi kontrolu
    private void checkDuplicateTreatmentPlans(Set<TreatmentPlan> treatmentPlans) {
        Set<String> uniqueTreatmentPlanDays = new HashSet<>();
        Set<LocalDateTime> existingTreatmentPlanStartDates = new HashSet<>();
        Set<LocalDateTime> existingTreatmentPlanStopDates = new HashSet<>();

        for (TreatmentPlan treatmentPlan : treatmentPlans) {
            String treatmentPlanDay = treatmentPlan.getDay().name();

            // !!! Karsilastirilan treatment planlar ayni gunde ise
            if (uniqueTreatmentPlanDays.contains(treatmentPlanDay)) {

                //!!! Baslama saatine gore kontrol
                for (LocalDateTime startDate : existingTreatmentPlanStartDates) {

                    //!!! Baslama saatleri esit ise
                    if (treatmentPlan.getStartDate().equals(startDate)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }

                    // !!! mevcut treatment Planın başlangıç saati ile diğer bir treatment Planın başlangıç ve bitiş saatleri arasında çakışma olduğunda hata fırlatır.
                    if (treatmentPlan.getStartDate().isBefore(startDate) && treatmentPlan.getEndDate().isAfter(startDate)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }
                }
                //!!! Bitis saatine gore kontrol
                for (LocalDateTime stopDate : existingTreatmentPlanStopDates) {
                    if (treatmentPlan.getStartDate().isBefore(stopDate) && treatmentPlan.getEndDate().isAfter(stopDate)) {
                        throw new BadRequestException(ErrorMessages.TREATMENT_PLAN_ALREADY_EXIST);
                    }
                }
            }
            //!!! yukardaki Baslangic ve Bitis saatine gore kontrollerden gecen TreatmenPlan, metodun en basinda olusturulan unique degiskenlere ataniyor

            uniqueTreatmentPlanDays.add(treatmentPlanDay);
            existingTreatmentPlanStartDates.add(treatmentPlan.getStartDate());
            existingTreatmentPlanStopDates.add(treatmentPlan.getEndDate());

        }

    }

    //!!!  mevcut treatment plan ile talep edilen treatment planları arasında karşılaştırma

    private void checkDuplicateTreatmentPlans(Set<TreatmentPlan> existTreatmentPlan,
                                              Set<TreatmentPlan> treatmentPlanRequest) {
        for (TreatmentPlan requestTreatmentPlan : treatmentPlanRequest) {
            String requestTreatmentPlanDay = requestTreatmentPlan.getDay().name();
            LocalDateTime requestStart = requestTreatmentPlan.getStartDate();
            LocalDateTime requestStop = requestTreatmentPlan.getEndDate();

            if (existTreatmentPlan.stream()
                    .anyMatch(treatmentPlan ->
                            treatmentPlan.getDay().name().equals(requestTreatmentPlan)
                                    && (treatmentPlan.getStartDate().equals(requestStart)
                                    || (treatmentPlan.getStartDate().isBefore(requestStart) && treatmentPlan.getEndDate().isAfter(requestStop))
                                    || (treatmentPlan.getStartDate().isAfter(requestStop) && treatmentPlan.getEndDate().isBefore(requestStop))
                                    || (treatmentPlan.getStartDate().isAfter(requestStart) && treatmentPlan.getEndDate().isBefore(requestStop))))
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
