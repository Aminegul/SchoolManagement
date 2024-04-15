package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.entity.user.User;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.businnes.ChooseLessonProgramWithId;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.businnes.LessonProgramService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;

    private final UniquePropertyValidator uniquePropertyValidator;

    private final UserMapper userMapper;

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    private final LessonProgramService lessonProgramService;

    private final DateTimeValidator dateTimeValidator;
    // NOT: saveStudent()*************************************************
    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest){

        // !!! Id kontrolü
        User advisorTeacher = userRepository.findById(studentRequest.getAdvisorTeacherId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,
                                                studentRequest.getAdvisorTeacherId())));
        // !!! acaba Advisor mı?
        if (advisorTeacher.getIsAdvisor()!=Boolean.TRUE){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE,
                                                advisorTeacher.getUserId()));
        }

        // !!! Uniqe kontrolu

        uniquePropertyValidator.checkDuplicate(studentRequest.getUsername(),
                                                studentRequest.getSsn(),
                                                studentRequest.getPhoneNumber(),
                                                studentRequest.getEmail());

        // !!! DTO -POJO döndürmemiz lazım
        User student = userMapper.mapStudentRequestToUser(studentRequest);

        student.setAdvisorTeacherId(advisorTeacher.getUserId());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setIsAdvisor(Boolean.FALSE);

        // !!! Öğrenci numarası setleniyor ---> yardımcı metod yapılacak getLastNumber
        student.setStudentNumber(getLastNumber());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(student)))
                .message(SuccessMessages.STUDENT_SAVE)
                .build();

    }

    private int getLastNumber(){
        if (!userRepository.findStudent(RoleType.STUDENT)){
            return  1000;
        }
        return  userRepository.getMaxStudentNumber() + 1 ;
    }

    // NOT: updateStudentForStudents()*************************************************
    public ResponseEntity<String> updateStudent(StudentRequestWithoutPassword studentRequestWithoutPassword, HttpServletRequest request) {
        String userName = (String) request.getAttribute("username"); // öncelikle username i bi almamız lazım
        User student = userRepository.findByUsernameEquals(userName);

        uniquePropertyValidator.checkUniqueProperties(student, studentRequestWithoutPassword);

        student.setUsername(studentRequestWithoutPassword.getUsername());
        student.setMotherName(studentRequestWithoutPassword.getMotherName());
        student.setFatherName(studentRequestWithoutPassword.getFatherName());
        student.setBirthDay(studentRequestWithoutPassword.getBirthDay());
        student.setEmail(studentRequestWithoutPassword.getEmail());
        student.setPhoneNumber(studentRequestWithoutPassword.getPhoneNumber());
        student.setBirthPlace(studentRequestWithoutPassword.getBirthPlace());
        student.setGender(studentRequestWithoutPassword.getGender());
        student.setName(studentRequestWithoutPassword.getName());
        student.setSurname(studentRequestWithoutPassword.getSurname());
        student.setSsn(studentRequestWithoutPassword.getSsn());

        userRepository.save(student);
        return ResponseEntity.ok(SuccessMessages.USER_UPDATE_MESSAGE);
    }

    // NOT: updateStudent()******************************************************
    public ResponseMessage<StudentResponse> updateStudentForManagers(Long userId, StudentRequest studentRequest) {


        User user = isUserExist(userId);

        if (!user.getUserRole().getRoleType().equals(RoleType.STUDENT)){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_STUDENT_MESSAGE));
        }

        uniquePropertyValidator.checkUniqueProperties(user, studentRequest);

        User updatedStudent = userMapper.mapStudentRequestToUpdatedUser(studentRequest, userId);

        updatedStudent.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        updatedStudent.setAdvisorTeacherId(studentRequest.getAdvisorTeacherId());

        updatedStudent.setStudentNumber(user.getStudentNumber());

        updatedStudent.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));

        updatedStudent.setActive(true);



        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(updatedStudent)))
                .message(SuccessMessages.STUDENT_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public User isUserExist(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    // NOT: addLessonProgramToStudentLessonProgram ********************************
    public ResponseMessage<StudentResponse> addLessonProgramToStudent(String userName,
                                                                      ChooseLessonProgramWithId chooseLessonProgramWithId) {
        // !!! username ile user getiriliyor
        User student = userRepository.findByUsernameEquals(userName);

        // Lesson programlar eklenecek - yapıldı
        //  !!! DTO da talep edilen LP ler getirildi
        Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(chooseLessonProgramWithId.getLessonProgramId());

        // !!! Öğrencinin mevcuttaki LessonProgram larını getiriyoruz
        Set<LessonProgram> studentCurrentLessonProgram = student.getLessonProgramList();

        // !!! Talep edilen ile mevcuttaki LessonProgramlarını karşılaştıracağız
        dateTimeValidator.checkLessonPrograms(studentCurrentLessonProgram, lessonProgramSet);

        studentCurrentLessonProgram.addAll(lessonProgramSet);

        student.setLessonProgramList(studentCurrentLessonProgram);

        User savedStudent = userRepository.save(student);

        return ResponseMessage.<StudentResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_ADD_TO_STUDENT)
                .object(userMapper.mapUserToStudentResponse(savedStudent))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // NOT: changeActiveStatusOfStudent **********************************************
    public ResponseMessage changeStatusOfStudent(Long studentId, boolean status) {
       User student = isUserExist(studentId);

       if (!student.getUserRole().getRoleType().equals(RoleType.STUDENT)){
           throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_STUDENT_MESSAGE, studentId));
       }

       student.setActive(status);
       userRepository.save(student);

       return ResponseMessage.builder()
               .message("Student is " + (status ? "active" : "passive"))
               .httpStatus(HttpStatus.OK)
               .build();
    }
}
