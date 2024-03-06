package com.project.service.validator;

import com.project.exception.ConflictException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.project.entity.concretes.user.User;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    public void checkUniqueProperties(User user, AbstractUserRequest abstractUserRequest) {
        //abstractUserRequest'dan gelen username,ssn,phone,email bilgileri db'deki user'ın bilgilerine eşit ise checkDuplicate'i çalıştırma demeliyiz.
        //Eğer birinde bir değişiklik var ise checkDuplicate'i çalıştırmamız gerekiyor.

        String updatedUsername = "";
        String updatedSsn = "";
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;

        if (!user.getUsername().equalsIgnoreCase(abstractUserRequest.getUsername())) {
            updatedUsername = abstractUserRequest.getUsername();
            isChanged = true;
        }

        if (!user.getSsn().equalsIgnoreCase(abstractUserRequest.getSsn())) {
            updatedSsn = abstractUserRequest.getSsn();
            isChanged = true;
        }

        if (!user.getPhoneNumber().equalsIgnoreCase(abstractUserRequest.getPhoneNumber())) {
            updatedPhone = abstractUserRequest.getPhoneNumber();
            isChanged = true;
        }

        if (!user.getEmail().equalsIgnoreCase(abstractUserRequest.getEmail())) {
            updatedEmail = abstractUserRequest.getEmail();
            isChanged = true;
        }

        if (isChanged) {
            checkDuplicate(updatedUsername, updatedSsn, updatedPhone, updatedEmail);
        }
    }

    public void checkDuplicate(String username, String ssn, String phone, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        } else if (userRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        } else if (userRepository.existsByPhone(phone)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE, phone));
        } else if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }

    }

}
