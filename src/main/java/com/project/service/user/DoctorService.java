package com.project.service.user;

import com.project.payload.request.user.DoctorRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.DoctorResponse;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UserRoleRepository userRoleRepository;

    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest doctorRequest) {

        return null;

    }
}
