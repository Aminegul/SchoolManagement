package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.contactmessage.exception.ConflictException;
import com.project.schoolmanagment.entity.user.User;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.abstracts.AbstractUserRequest;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor //@RequiredArgsConstructor: Class içinde final ve NonNull olan değişkenleri parametre olarak alan bir constructor oluşturur.------->>> private final UserRepository userRepository; bunun için yapıldı
public class UniquePropertyValidator {  //BU SINIF NEDEN YAPILDI????????????

    private final UserRepository userRepository;

    // NOT: updateAdminOrDeanOrViceDean**************************** kısmında service kısmından buraya geldik
    // Requestten gelen fieldlar ile DB de ki User'ın fieldları aynı ise -->> checkDuplicate yapma ama dördünden biri bile değiştiyse checkDuplicate yap dedik
    public void checkUniqueProperties(User user, AbstractUserRequest baseUserRequest){ // BaseUserRequest i -> AbstractUserRequest
        String updatedUsername = ""; //initialize yapmak yani objeyi başlatmak
        String updatedSsn = "";
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;

        if (!user.getUsername().equalsIgnoreCase(baseUserRequest.getUsername())){
            updatedUsername = baseUserRequest.getUsername();
            isChanged = true;
        }

        if (!user.getSsn().equalsIgnoreCase(baseUserRequest.getSsn())){
            updatedSsn= baseUserRequest.getSsn();
            isChanged = true;
        }

        if (!user.getPhoneNumber().equalsIgnoreCase(baseUserRequest.getPhoneNumber())){
            updatedPhone= baseUserRequest.getPhoneNumber();
            isChanged = true;
        }
        if (!user.getEmail().equalsIgnoreCase(baseUserRequest.getEmail())){
            updatedEmail= baseUserRequest.getEmail();
            isChanged = true;
        }

        if (isChanged){
            checkDuplicate(updatedUsername,updatedSsn,updatedPhone,updatedEmail);
        }

        // checkUniqueProperties --> metodu user ve baseUserRequest'in fieldlarını karşılaştırır.
        // fieldlar eşit değilse isChanged true olur
        // isChanged true olduğu durumda ise checkDuplicate metodu çağırılır
        // checkDuplicate metodu da baseUserRequest fieldlarının database de olup olmadıklarını kontrol eder.
    }

    public  void checkDuplicate(String username, String ssn, String phone,String email){
        if(userRepository.existsByUsername(username)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME,username));
        }
        if (userRepository.existsBySsn(ssn)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN,ssn));
        }
        if (userRepository.existsByEmail(email)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL,email));
        }
        if (userRepository.existsByPhoneNumber(phone)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER,phone));
        }
    }
}
