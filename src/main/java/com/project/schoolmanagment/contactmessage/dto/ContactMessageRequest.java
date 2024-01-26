package com.project.schoolmanagment.contactmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder (toBuilder = true)

public class ContactMessageRequest {

    //Requestlerimiz;

    @NotNull (message = "Please enter name")
    @Size (min=3 , max=16 , message = "Your name should be at least 3 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your name must consist of the character.")
    private String name;

    @NotNull (message = "Please enter email")
    @Size (min=5 , max=20 , message = "Your email should be at least 5 chars")
    @Email(message = "Please enter valid email")
    //@Column (nullable = false, unique = true, length = ) Entity olmadığı için buna gerek yok DTO ların içinde entity class olmaz
    private String email;

    @NotNull (message = "Please enter subject")
    @Size (min=3 , max=20 , message = "Your subject should be at least 3 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your subject must consist of the character.")
    private String subject;

    @NotNull (message = "Please enter name")
    @Size (min=4 , max=50 , message = "Your message should be at least 3 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your message must consist of the character.")
    private String message;

}
