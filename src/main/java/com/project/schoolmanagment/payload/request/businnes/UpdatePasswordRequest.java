package com.project.schoolmanagment.payload.request.businnes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "Please provide old password") //NotBlank-string ifadeler için kullanılıyor NotNull ı da kapsıyor
    private String oldPassword;
    @NotBlank(message = "Please provide new password")
    private String newPassword;

}
