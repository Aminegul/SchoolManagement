package com.project.schoolmanagment.contactmessage.mapper;

import com.project.schoolmanagment.contactmessage.dto.ContactMessageRequest;
import com.project.schoolmanagment.contactmessage.dto.ContactMessageResponse;
import com.project.schoolmanagment.contactmessage.entity.ContactMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component //contact message mapper objesi spring bootta kullanılacak bean dir. Bean Java memory de bir tane obje oluşturulur ve her zaman aynı obje kullanılır.
public class ContactMessageMapper {

    //PostMapping ve PutMapping( Update ) için requestDTO al entitye çevir database e kaydet
    public ContactMessage mapContactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest){

        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .email(contactMessageRequest.getEmail())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

    }

    //GetMapping databaseden entityi al responsa çevir Client'a göster
    public ContactMessageResponse mapContactMessageToContactMessageResponse (ContactMessage contactMessage){

        return ContactMessageResponse.builder() //builder sayesinde böyle yazabildik.
                .name(contactMessage.getName())
                .email(contactMessage.getEmail())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

    }


}
