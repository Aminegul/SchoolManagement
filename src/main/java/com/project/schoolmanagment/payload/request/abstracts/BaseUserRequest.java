package com.project.schoolmanagment.payload.request.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUserRequest extends AbstractUserRequest{

    @NotNull(message = "Please enter your password")
    @Size(min = 8,max = 60,message = "Your password should be between 8-60 characters")
    private String password;

    private Boolean builtIn;

}
