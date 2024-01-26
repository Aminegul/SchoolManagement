package com.project.schoolmanagment.payload.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

public class UserRequest extends BaseUserRequest { //

}
