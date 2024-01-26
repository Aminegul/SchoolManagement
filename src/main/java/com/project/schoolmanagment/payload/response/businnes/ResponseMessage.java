package com.project.schoolmanagment.payload.response.businnes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity yazmamıza gerek yok DB ye kaydedilecekse yazmamız gerekir boş yere tablo oluşur
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL) //dosyanın içerisinde bulunmasını istediğim ya da istemediğim yapıları bu anatationla spring frameworke söylüyorum
public class ResponseMessage<E> { //generic olması lazım her türden mesaj döndürmesi için

    private E object; //E data türünde bir obje
    private HttpStatus httpStatus;
    private String message;

}
