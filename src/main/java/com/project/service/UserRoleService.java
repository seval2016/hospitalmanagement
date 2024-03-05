package com.project.service;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.payload.messages.ErrorMessages;

import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRole(RoleType roleType){
        return userRoleRepository.findByEnumRoleEquals(roleType).orElseThrow(
                ()-> new ResolutionException(ErrorMessages.ROLE_NOT_FOUND)
        );
    }

    public List<UserRole> getAllUserRoles(){
        return userRoleRepository.findAll();
    }
}