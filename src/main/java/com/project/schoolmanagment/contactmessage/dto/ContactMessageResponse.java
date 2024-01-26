package com.project.schoolmanagment.contactmessage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder (toBuilder = true)

public class ContactMessageResponse { //doğrulamaya gerek yok çünkü data base de olanı gösteriyoruz yani NotNull gerek yok

    private String name;
    private String email;
    private String subject;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime dateTime;

}
