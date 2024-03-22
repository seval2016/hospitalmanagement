package com.project.repository.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.response.user.PatientResponse;
import com.project.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.DoubleStream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameEquals(String username);

    User findByUsername(String Username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    List<User> getUserByNameContaining(String name);

    @Query(value = "SELECT COUNT(u) FROM User u WHERE u.userRole.roleType = ?1")
    long countAdmin(RoleType roleType);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName = ?1")
    Page<User> findByUserByRole(String roleName, Pageable pageable);

    List<User> findByPatientDoctorId(Long id);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName='DOCTOR'")
    List<User> findAllDoctor();

    @Query("select (Count (u) > 0 ) FROM User u WHERE u.userRole.roleName= ?1")
    boolean findPatient(RoleType roleType);

    @Query(value = "SELECT MAX (u.patientNumber) FROM User u")
    int getMaxStudentNumber();


}