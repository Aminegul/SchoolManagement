package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.entity.user.User;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;

import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.request.user.UserRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;


    // NOT: saveUser()*************TEACHER VE STUDENT HARİÇ***************** (ADMİN, DEAN, ASİSTANT MENAGER)
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {
        // !!! unique kontrolü
        uniquePropertyValidator.checkDuplicate(
                userRequest.getUsername(),
                userRequest.getSsn(),
                userRequest.getPhoneNumber(),
                userRequest.getEmail());

        // !!! DTO --> POJO
        // userRequest objesi bir DTO objesidir ---> Data Base e bunu entity objesi olarak kaydetmemiz lazım
        //Bunun için DTO objesini Entity objesine çeviren "mapUserRequestToUser" metodunu kullanıyoruz.
        User user = userMapper.mapUserRequestToUser(userRequest);

        // !!! Role bilgisi setlenecek
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.name())) {
            if (Objects.equals(userRequest.getUsername(), "Admin")) {
                user.setBuiltIn(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        } else if (userRole.equalsIgnoreCase("Dean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        } else {
            throw new ResourceNotFoundException(String.format(
                    ErrorMessages.NOT_FOUND_USER_USER_ROLE_MESSAGE, userRole));
        }
        // !!! Password encode edilecek
        //this line should be written after security
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // !!! isAdvisor ---> False
        user.setIsAdvisor(false);

        // DB'ye kaydediliyor
        User savedUser = userRepository.save(user);

        //Response nesnesi oluşturuluyor
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATE)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }


    // NOT: getAllAdminOrDeanOrViceDean()**************************************
    public Page<UserResponse> getUserByPage(int page, int size, String sort, String type, String userRole) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return userRepository.findByUserByRole(userRole, pageable).map(userMapper::mapUserToUserResponse);

    }

    //NOT: getUserById() ****************************************************
    public ResponseMessage<BaseUserResponse> getUserById(Long userId) {

        BaseUserResponse baseUserResponse = null;

        // !!! id var mı?
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));

        if (user.getUserRole().getRoleType() == RoleType.STUDENT) {
            baseUserResponse = userMapper.mapUserToStudentResponse(user);
        } else if (user.getUserRole().getRoleType() == RoleType.TEACHER) {
            baseUserResponse = userMapper.mapUserToTeacherResponse(user);
        } else
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(baseUserResponse)
                .build();
    }

    // NOT: deleteUser()******************************************************
    public String deleteUserById(Long id, HttpServletRequest request) {
        // !!! Id kontrol
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, id)));
        // !!! bu metodu tetikleyen kullanıcıya ulaşıyoruz
        String userName = (String) request.getAttribute("username");
        User user2 = userRepository.findByUsernameEquals(userName); // user2 --> silme talebinde bulunan user

        //built-in kontrolü
        if (Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);

        } else if (user2.getUserRole().getRoleType() == RoleType.MANAGER) {

            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT) ||
                    (user.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER))) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        } else if (user2.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER) {

            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT))) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }
        userRepository.deleteById(id);
        return SuccessMessages.USER_DELETE;

    }

    // NOT: updateAdminOrDeanOrViceDean**************************************
    public ResponseMessage<BaseUserResponse> updateUser(UserRequest userRequest, Long userId) {

        // !!! id kontrol
        User user = isUserExist(userId); // bunu istediğim İd kontrolde kullanabilirim

        // TODO :BUİLT-İN kontrolü

        //uniq kontrolü - başka kullanıcılarda var mı diye bu 4 ünü kontrol etmek zorundayız

        // uniquePropertyValidator.checkDuplicate(userRequest.getUsername(), userRequest.getSsn(), userRequest.getPhoneNumber(), userRequest.getEmail());
        // bu kısım gereksiz çünkü uniq kontrolü yapıyoruz ancak bu metodda bunu çağırmamız gerekli değil çünkü hepsini tek tek kontrol etmemize gerek yok
        //bu yüzden UniquePropertyValidator classında ayrı bi metod yazıp Requeste gelen fieldlar ile DB de ki User'ın fieldları aynı ise
        // ------->> checkDuplicate yapma ama dördünden biri bile değiştiyse checkDuplicate yap dedik

        uniquePropertyValidator.checkUniqueProperties(user, userRequest);

        // !!! DTO ---> POJO
        User updatedUser = userMapper.mapUserRequestToUpdatedUser(userRequest, userId);

        // !!! password encoded
        updatedUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));


        User savedUser = userRepository.save(updatedUser);

        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_UPDATE_MESSAGE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }

    public User isUserExist(Long userId) { // Servis katında bir metod pojo classı döndürüyorsa bunu sebebi ne olabilir?
        // ya başka bir servis tarafından kullanılcaktır ya da içinde bulunduğumuz servis tarafından kullanılacaktır - helper metod
        //return userRepository.findById(userId).orElseThrow(()->
        //new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));

        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    // NOT: updateUserForUsers()********************************************
    public ResponseEntity<String> updateUserForUsers(UserRequestWithoutPassword userRequest,
                                                     HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");
        // getAttribute metodu giriş yapan user'ın username fieldını bize String olarak döndürecek
        // getAttribute -> bu mtod obje dönderdiği için (String) yazıyoruz başa ve String ifade ye çevirmiş oluyoruz. Yani TypeCasting.

        User user = userRepository.findByUsernameEquals(userName);

        // TODO: built-in kontrolü

        // !!! unique kontrolü
        uniquePropertyValidator.checkUniqueProperties(user, userRequest);

        // !!! update işlemi
        user.setBirthDay(userRequest.getBirthDay());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setGender(userRequest.getGender());
        user.setBirthPlace(userRequest.getBirthPlace());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setSsn(userRequest.getSsn());
        user.setUsername(userRequest.getUsername());

        userRepository.save(user);
        String message = SuccessMessages.USER_UPDATE_MESSAGE;
        return ResponseEntity.ok(message);


    }

    // NOT: getByName()************************************************ kullanıcın ismi şu olan verileri getir
    public List<UserResponse> getUserByName(String name) {

        // ?????????????????????????????????????????????   UserRepository classı içinde getUserByNameContaining
        return userRepository.getUserByNameContaining(name) // POJO dönecekti onu DTO ya çevircem
                .stream() //aktarım -
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());

        // BU YAPI : Java Streamden gelen bi yapı. Bize hazır metodlar sunar. Örneğin map methodu eşleştirme metodudur.
    }

    // -----------------------------------------------------

    // NOT: Runner için yazıldı ************************************************
    public long countAllAdmins(){
        return userRepository.countAdmin(RoleType.ADMIN);
    }
}



