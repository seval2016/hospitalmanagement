package com.project.controller.user;

import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //!!! Save --> Doctor ve Patient dışındakiler için
    @PostMapping("save/{userRole}") // http://localhost:8080/user/save/Admin + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(@RequestBody @Valid UserRequest userRequest,
                                                                  @PathVariable String userRole) {
        return ResponseEntity.ok(userService.saveUser(userRequest, userRole));
    }

    @DeleteMapping("/delete/{id}") //http://localhost:8080/user/delete/3
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(userService.deleteUserById(id, request));
    }

    //Update işlemi
    //!!! Admin -->Manager yada Assistan manager için yazılan update kodu
    //!!! Doctor ve patient için ekstra fieldlar gerekeceği için başka endpoint gerekiyor.
    @PutMapping("/update/{userId}") //http://localhost:8080/user/update/4
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BaseUserResponse> updateAdminManagerAndManagerAssistantForAdmin(@RequestBody @Valid UserRequest userRequest, @PathVariable Long userId) {
        return userService.updateUser(userRequest, userId);
    }

}
