package com.project.schoolmanagment.payload.response.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  //null olamayan yapılan bu json dosyanın içerisinde olsun
public class AuthResponse {

    private String username;
    private String ssn;
    private String role;
    private String token;  //Özel kod oluşturuyor postman - security ile yapıldı
    private String name;

}
