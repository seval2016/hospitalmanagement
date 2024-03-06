package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.UserRoleService;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;

    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {

        //!!! username-ssn-phoneNumber-email unique mi ??
        uniquePropertyValidator.checkDuplicate(userRequest.getUsername(), userRequest.getSsn(), userRequest.getPhoneNumber(), userRequest.getEmail());

        // !!! DTO -> POJO
        User user = userMapper.mapUserRequestToUser(userRequest);

        //!!! Rol Bilgisi

        if (userRole.equalsIgnoreCase(RoleType.ADMIN.name())) {

            if (Objects.equals(userRequest.getUsername(), "SuperAdmin")) {
                user.setBuilt_in(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        } else if (userRole.equalsIgnoreCase("Dean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        } else {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_USERROLE_MESSAGE, userRole));
        }

        //!!! Password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //!!! isChiefDoctor degerini False yapiyoruz
        user.setIsChiefDoctor(Boolean.FALSE);
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }

    public String deleteUserById(Long id, HttpServletRequest request) {

        // !!! silinecek user var mı kontrolü
        User user = methodHelper.isUserExist(id);

        //!!! methodu tetikleyen yani istek yapan kullanıcının rol bilgisini alma
        String username = (String) request.getAttribute("username");
        User user2 = userRepository.findByUsernameEquals(username);

        //!!! builtin ve rol kontrolü

        if (Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            //MANAGER sadece Doctor,patient,Assistant_Manager silebilir
        } else if (user2.getUserRole().getRoleType() == RoleType.MANAGER) {//silme talebinde bulunan kişinin rolü manager ise yalnızca Doctor,patient,Assistant_Manager silebilir
            if (!(
                    (user.getUserRole().getRoleType() == RoleType.DOCTOR) ||
                            (user.getUserRole().getRoleType() == RoleType.PATIENT) ||
                            (user.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER)
            )) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE); //silmek istenilenler  Doctor,patient,Assistant_Manager değil ise hata fırlat
            }

            //Mudur yardımcısı sadece doctor veya patient silebilir
        } else if (user2.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER) {
            if (!(
                    (user.getUserRole().getRoleType() == RoleType.DOCTOR) ||
                            (user.getUserRole().getRoleType() == RoleType.PATIENT)
            )) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }
        userRepository.deleteById(id);
        return SuccessMessages.USER_DELETE;
    }


    public ResponseMessage<BaseUserResponse> updateUser(UserRequest userRequest, Long userId) {

        // !!! update edilecek user var mı kontrolü
        User user = methodHelper.isUserExist(userId);

        // !!! built_in mi kontrolü
        methodHelper.checkBuiltIn(user);

        //!!! duplicate control yani username-ssn-phoneNumber-email unique mi ??
        uniquePropertyValidator.checkDuplicate(
                                            userRequest.getUsername(),
                                            userRequest.getSsn(),
                                            userRequest.getPhoneNumber(),
                                            userRequest.getEmail());


    }
}
