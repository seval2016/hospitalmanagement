package com.project;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.Gender;
import com.project.entity.enums.RoleType;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRoleRepository;
import com.project.service.UserRoleService;
import com.project.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

//!!! Uygulama run edilir edilmez, Rol tablom bos olacagi icin gerekli rollerin eklenmesini saglamak
// icin bu sinifimizi CommandLineRunner interface inden implement ediyoruz ve icindeki run metodunu
// override etmemiz gerekiyor.
@SpringBootApplication
public class HospitalManagementApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;
	private final UserService userService;

	public HospitalManagementApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementApplication.class, args);
	}

	@Override //uygulama her çalıştığında bu kodlar otomatik olarak çalışacak
	public void run(String... args) throws Exception {
		//!!! Role tablosunu roller ile dolduracağız
		if(userRoleService.getAllUserRole().isEmpty()) {

			UserRole admin = new UserRole();
			admin.setRoleName("Admin");
			admin.setRoleType(RoleType.ADMIN);
			userRoleRepository.save(admin);//Admin rolünü database'e kaydını yaptık

			UserRole manager = new UserRole();
			manager.setRoleType(RoleType.MANAGER);
			manager.setRoleName("Manager");
			userRoleRepository.save(manager);//manager rolünü database'e kaydını yaptık

			UserRole assistenManager = new UserRole();
			assistenManager.setRoleType(RoleType.ASSISTANT_MANAGER);
			assistenManager.setRoleName("AssistenManager");
			userRoleRepository.save(assistenManager);

			UserRole patient = new UserRole();
			patient.setRoleType(RoleType.PATIENT);
			patient.setRoleName("Patient");
			userRoleRepository.save(patient);

			UserRole doctor = new UserRole();
			doctor.setRoleType(RoleType.DOCTOR);
			doctor.setRoleName("Doctor");
			userRoleRepository.save(doctor);

		}

		//!!! user tablosunda built_in ADMIN olusturacagiz
		if(userService.countAllAdmins() == 0) {//Bu koşulda hiçbir admin rolüne sahip kullanıcı yok ise;
			UserRequest adminRequest = new UserRequest();
			adminRequest.setUsername("Admin");//eğer hiç built_in kullanıcı yok ise otomatik built_in olacağı için Admin diye yazılır sadece bu kod parçasında böyle olmalı daha sonrasındaki user'larda farklı isim verilebilir.
			adminRequest.setEmail("admin@admin.com");
			adminRequest.setSsn("111-11-1111");
			adminRequest.setPassword("12345678");
			adminRequest.setName("Kenan");
			adminRequest.setSurname("Yılmaz");
			adminRequest.setPhoneNumber("444-333-1212");
			adminRequest.setGender(Gender.MALE);
			adminRequest.setBirthDay(LocalDate.of(1993,5,30));
			adminRequest.setBirthPlace("California");

			userService.saveUser(adminRequest, "Admin");
		}

	}
}
