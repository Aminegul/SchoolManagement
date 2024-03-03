package com.project.schoolmanagment;

import com.project.schoolmanagment.entity.enums.Gender;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.entity.user.UserRole;
import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.repository.user.UserRoleRepository;
import com.project.schoolmanagment.service.user.UserRoleService;
import com.project.schoolmanagment.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SchoolManagementApplication implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;

    private final UserService userService;

    public SchoolManagementApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserRepository userRepository, UserService userService) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementApplication.class, args);
        //this is our first day

    }

   @Override
    public void run(String... args) throws Exception {
        // !!! Role tablomu dolduracağım
        if (userRoleService.getAllUserRole().isEmpty()) {

            UserRole admin = new UserRole();
            admin.setRoleType(RoleType.ADMIN);
            admin.setRoleName("Admin");
            userRoleRepository.save(admin);

            UserRole dean = new UserRole();
            dean.setRoleType(RoleType.MANAGER);
            dean.setRoleName("Dean");
            userRoleRepository.save(dean);

            UserRole viceDean = new UserRole();
            viceDean.setRoleType(RoleType.ASSISTANT_MANAGER);
            viceDean.setRoleName("ViceDean");
            userRoleRepository.save(viceDean);

            UserRole student = new UserRole();
            student.setRoleType(RoleType.STUDENT);
            student.setRoleName("student");
            userRoleRepository.save(student);

            UserRole teacher = new UserRole();
            teacher.setRoleType(RoleType.TEACHER);
            teacher.setRoleName("teacher");
            userRoleRepository.save(teacher);

        }


        // !!! Built_in Admin oluşturuluyor
       if (userService.countAllAdmins() == 0) {
            UserRequest adminRequest = new UserRequest();
            adminRequest.setUsername("Admin");
            adminRequest.setEmail("aaa@bbb.com");
            adminRequest.setSsn("111-11-1111");
            adminRequest.setPassword("12345678");
            adminRequest.setName("Ahmet");
            adminRequest.setSurname("Ahmet");
            adminRequest.setPhoneNumber("1111-1111-1111");
            adminRequest.setGender(Gender.MALE);
            adminRequest.setBirthDay(LocalDate.of(1980, 2, 2));
            adminRequest.setBirthPlace("Texas");

            userService.saveUser(adminRequest, "Admin");
        }

    }



}













