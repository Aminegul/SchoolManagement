package com.project.schoolmanagment.payload.request.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.schoolmanagment.entity.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;

@SuperBuilder
@Data
@NoArgsConstructor
public class AbstractUserRequest {

    @NotNull(message = "Please enter your username")
    @Size (min = 4, max = 16, message = "Your username should be at least 4 chars")
    @Pattern(regexp = "\\a(?!\\s*\\z).+", message = "Your username must consist of the characters.")
    private String username;

    @NotNull(message = "Please enter your name")
    @Size (min = 3, max = 16, message = "Your name should be at least 4 chars")
    @Pattern(regexp = "\\a(?!\\s*\\z).+", message = "Your name must consist of the characters.")
    private String name;

    @NotNull(message = "Please enter your surname")
    @Size (min = 2, max = 16, message = "Your surname should be at least 4 chars")
    @Pattern(regexp = "\\a(?!\\s*\\z).+", message = "Your surname must consist of the characters.")
    private String surname;

    @NotNull(message = "Please enter your surname")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Your birthday can not be in the future.")
    private LocalDate birthDay;

    @NotNull
    @Pattern(regexp = "^(?!666|000|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4}$",
            message = "Please enter valid SSN number")
    private String ssn;

    @NotNull(message = "Please enter your birthplace")
    @Size (min = 2, max = 16, message = "Your birthplace should be at least 4 chars")
    @Pattern(regexp = "\\a(?!\\s*\\z).+",
            message = "Your birthplace must consist of the characters.")
    private String birthPlace;

    @NotNull(message = "Please enter your gender")
    private Gender gender;

    @NotNull(message = "Please enter your phone number")
    @Size (min = 12, max = 12, message = "Your phone number should be 12 characters long")
    @Pattern(regexp = "[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "Please enter valid phone number.")
    private String phoneNumber;

    @NotNull(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Size (min = 5, max = 50, message = "Your email shpuld be between 5 and 50 chars")
    private String email;

    private Boolean builtIn =false; //???????

}
