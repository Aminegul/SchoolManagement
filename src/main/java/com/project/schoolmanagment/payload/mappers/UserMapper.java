package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.user.User;
import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User mapUserRequestToUser(BaseUserRequest userRequest){
        // userRequest objesini kullanarak User entity objesi oluşturuyoruz.
        // DTO objesini Entity objesine çeviren "mapUserRequestToUser" metod
        return User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .builtIn(userRequest.getBuiltIn())
                .build();
    }

    public User mapUserRequestToUpdatedUser(UserRequest userRequest, Long userId){
        return User.builder()
                .userId(userId)
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .builtIn(userRequest.getBuiltIn())
                .build();

    }

    public StudentResponse mapUserToStudentResponse(User student) {
        return StudentResponse.builder()
                .userId(student.getUserId())
                .username(student.getUsername())
                .name(student.getName())
                .surname(student.getSurname())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .lessonProgramSet(student.getLessonProgramList())
                .build();
    }


    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();

    }
    public TeacherResponse mapUserToTeacherResponse(User teacher){
        return TeacherResponse.builder()
                .userId(teacher.getUserId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .lessonPrograms(teacher.getLessonProgramList())
                .isAdvisorTeacher(teacher.getIsAdvisor())
                .build();
    }

    public User mapTeacherRequestToUser(TeacherRequest teacherRequest){

        return  User.builder()
                .name(teacherRequest.getName())
                .username(teacherRequest.getUsername())
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .birthDay(teacherRequest.getBirthDay())
                .birthPlace(teacherRequest.getBirthPlace())
                .ssn(teacherRequest.getSsn())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .gender(teacherRequest.getGender())
                .email(teacherRequest.getEmail())
                .isAdvisor(teacherRequest.getIsAdvisorTeacher())
                .builtIn(teacherRequest.getBuiltIn())
                .build();
    }

    public User mapTeacherRequestToUpdatedUser(TeacherRequest userRequest, Long userId){

        return  User.builder()
                .userId(userId)
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .isAdvisor(userRequest.getIsAdvisorTeacher())
                .build();
    }

    public User mapStudentRequestToUser(StudentRequest studentRequest){
        return User.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .username(studentRequest.getUsername())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .email(studentRequest.getEmail())
                .password(studentRequest.getPassword())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .builtIn(studentRequest.getBuiltIn())
                .build();
    }

    public User mapStudentRequestToUpdatedUser(StudentRequest studentRequest, Long userId){
        //we called other mapper not to write duplicated code
        User student = mapStudentRequestToUser(studentRequest);
        student.setUserId(userId);
        return student;
    }
}
