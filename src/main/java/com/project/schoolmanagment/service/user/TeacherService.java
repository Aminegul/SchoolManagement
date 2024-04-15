package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.contactmessage.exception.ConflictException;
import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.entity.user.User;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //Repository katmanıyla injektion işlemi yapılacağı için ve Class içinde final ve NonNull olan değişkenleri parametre olarak alan bir constructor oluşturur.
public class TeacherService {

    private final UserRepository userRepository;

    private final UniquePropertyValidator uniquePropertyValidator;

    private final UserMapper userMapper;

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    //NOT: saveTeacher()**************************************************
    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        //TODO: lessonProgramController yazılınca eklenecek

        //!!! Uniqe kontrolü --> injektion --> private final UniquePropertyValidator uniquePropertyValidator;
        uniquePropertyValidator.checkDuplicate(
                teacherRequest.getUsername(),
                teacherRequest.getSsn(),
                teacherRequest.getPhoneNumber(),
                teacherRequest.getEmail());

        // !!! DTO ---> POJO
        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);

        // !!! POJO da olması gereken ancak DTO 'dan gelmeyen değerleri setliyoruz. yani Role bilgisi setlenecek.

        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        //TODO: LessonProgram setlenecek

        // !!! password encode
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

        //!!! isAdvisor

        if (teacherRequest.getIsAdvisorTeacher()) {
            teacher.setIsAdvisor(Boolean.TRUE);
        } else teacher.setIsAdvisor(Boolean.FALSE);

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();
    }

    //NOT: updateTeacherById()**************************************************
    public ResponseMessage<TeacherResponse> updateTeacherForManager(TeacherRequest teacherRequest, Long userId) {

        // !!! Id kontrolü yani bu kullanıcı var mı yok mu (yardımcı metos kurduk aşağıya  --> isUserExist)
        User user = isUserExist(userId);

        // !!! parametrede gelen id bir teachera mı ait kontrolü
        if (!(user.getUserRole().getRoleType().equals(RoleType.TEACHER))) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_TEACHER_MESSAGE, userId));
        }

        // TODO : LessonProgram eklenecek

        // Uniqe kontrolü
        uniquePropertyValidator.checkUniqueProperties(user, teacherRequest);

        // !!! DTO ---> POJO
        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedUser(teacherRequest, userId);

        // !!! password encode
        updatedTeacher.setPassword(passwordEncoder.encode(updatedTeacher.getPassword()));

        // TODO: LessonProgram getirilecek

        //Role setlenecek
        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        //isAdvisor ı yazmıcaz çünkü bir öğretmeni advisor teacher a geçir diye bi şey yapılacak

        // save
        User savedTeacher = userRepository.save(updatedTeacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_UPDATE_MESSAGE)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public User isUserExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    //NOT: getAllStudentByAdvTeacherUserName()**************************************************
    public List<StudentResponse> getAllStudentByAdvisorUsername(String userName) {
        User teacher = getTeacherByUsername(userName); //yardımcı metod yazıyoruz bu username ile getir diye --->

        //verilen usernamein sahibi olan user Advisor mi?
        if (Boolean.FALSE.equals(teacher.getIsAdvisor())) {
            throw new BadRequestException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME));
        }

        //user lar dönüyor yani POJO dönüyor bizim bunu StudentResponse çevirmemiz lazım -->collection
        return userRepository.findByAdvisorTeacherId(teacher.getUserId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());
    }


    public User getTeacherByUsername(String teacherUsername){
        return userRepository.findByUsernameEquals(teacherUsername);
    }

    // NOT: SaveAdvisorTeacherByTeacherId()**************************************
    public ResponseMessage<UserResponse> saveAdvisorTeacher(Long teacherId) {

        //!!! Id var mı?? daha önce yazmıştık yardımcı metod ---> isUserExist();
        User teacher = isUserExist(teacherId);

        //!!! id ile gelen user , teacher mı?

        if (!(teacher.getUserRole().getRoleType().equals(RoleType.TEACHER))){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE, teacherId));
        }
        // !!! zaten Advisor ise
        if (Boolean.TRUE.equals(teacher.getIsAdvisor())){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_EXIST_ADVISOR_MESSAGE, teacherId));
        }

        teacher.setIsAdvisor(Boolean.TRUE);
        userRepository.save(teacher);

        return  ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_SAVE)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // NOT: deleteAdvisorTeacherById()******************************************
    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(Long teacherId) {
        // !!! id var mı
        User teacher = isUserExist(teacherId);

        // !!! id ile gelen user teacher mı
        if (!(teacher.getUserRole().getRoleType().equals(RoleType.TEACHER))){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE, teacherId));
        }

        // !!! zaten Advisor değilse
        if (Boolean.FALSE.equals(teacher.getIsAdvisor())){
            throw new ConflictException(String.format(ErrorMessages.NOT_EXIST_ADVISOR_MESSAGE, teacherId));
        }

        teacher.setIsAdvisor(Boolean.FALSE);
        userRepository.save(teacher);

        // !!! silinen advisor Teacherın studentları bu ilişkiyi koparmamız gerekiyor
        List<User> allStudent = userRepository.findByAdvisorTeacherId(teacherId);
        if(!allStudent.isEmpty()){
            allStudent.forEach(students->students.setAdvisorTeacherId(null));
        }


        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETE)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // NOT: getAllAdvisorTeacher()******************************************
    public List<UserResponse> getAllAdvisorTeacher() {
        //UserRepository den geleceklerin hepsi POJO bunu DTO ya çevirmemiz lazım //???????????????????????????

        return userRepository.findAllByAdvisor(Boolean.TRUE)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());

    }


}
