package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.businnes.UpdatePasswordRequest;
import com.project.schoolmanagment.payload.response.authentication.AuthResponse;
import com.project.schoolmanagment.payload.request.authentication.LoginRequest;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController { //  Authentication - WHO ARE U? **********  Authorization  - WHAT CAN YOU DO WİTH THİS APPLICATİON?


    private final AuthenticationService authenticationService;

    //NOT: Login() **********************************
    @PostMapping("/login") //http://localhost:8091/auth/login + POST +JSON
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest){
        return authenticationService.authenticateUser(loginRequest);
    }


    //NOT: findByUsername()*********************************
    @GetMapping("/user") //http://localhost:8091/auth/user  + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASISTANT_MANAGER','TEACHER','STUDENT')") //sisteme tontike edilmiş herhangi bir kulllanıcı bu metodu tetikleyebilsin - kayıtlı olmayan kişiler bunu aratamasın diye
    public ResponseEntity<UserResponse> findByUsername(HttpServletRequest request){ //bu Spring frameworkten gelen bir class mvc dersinde görülmüş
        String username = (String) request.getAttribute("username");//service kısmında set etmiştik requesti
        UserResponse userResponse = authenticationService.findByUsername(username);
        return ResponseEntity.ok(userResponse); //200 kod
    }



    //NOT: updatePassword()*********************************
    @PatchMapping("/updatePassword") //http://localhost:8091/auth/updatePassword  + GET
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                 HttpServletRequest request){

        authenticationService.updatePassword(updatePasswordRequest, request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE;
        return  ResponseEntity.ok(response);

    }


}
