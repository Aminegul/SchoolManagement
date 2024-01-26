package com.project.schoolmanagment.contactmessage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity

//@Data //getter setter yerine kullanıldı- ya da to string hashcode equals gibi anotationslar da içinde var
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true) //
public class ContactMessage { //entity objemiz DB de tablosu oluşturulacak

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email (message = "Please enter valid email!")
    private String email;

    @NotNull
    private String subject;

    @NotNull
    private String message;

    //2025-06-05
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime dateTime;

}
