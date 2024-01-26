package com.project.schoolmanagment.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
// @AllArgsConstructor gerek yok super builder bunu karşılıyor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseUserResponse { //burayı extends edince BaseUserResponse clasındaki tüm fieldlar buraya geldi

    //Burası DTO

    //ADMİN DEAN veya VICE DEAN BURASI
}
