package com.project.schoolmanagment.contactmessage.controller;

import com.project.schoolmanagment.contactmessage.dto.ContactMessageRequest;
import com.project.schoolmanagment.contactmessage.dto.ContactMessageResponse;
import com.project.schoolmanagment.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.schoolmanagment.contactmessage.entity.ContactMessage;
import com.project.schoolmanagment.contactmessage.service.ContactMessageService;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping ("/contactMessages")
@RequiredArgsConstructor //constructor injection @AutoWired a gerek kalmadı
public class ContactMessageController {

    private final ContactMessageService contactMessageService; //injection yaptık

    //Not: save() ************************************************
    @PostMapping ("/save") // http://localhost:8091/contactMessages/save  + POST + JSON  //yeni bir veri kaydedilecek
    public ResponseMessage<ContactMessageResponse> save (@RequestBody @Valid ContactMessageRequest contactMessageRequest){
        return contactMessageService.save(contactMessageRequest);
    }

    //BU KISMI BURHANLA YAZDIK
  /*  @PostMapping ("/save") // http://localhost:8091/contactMessages/save  + POST + JSON  //yeni bir veri kaydedilecek
    public ResponseEntity<ContactMessageResponse> saveMessage (@RequestBody @Valid ContactMessageRequest contactMessageRequest){
        return contactMessageService.save(contactMessageRequest);
    }*/

    //Not: getAll() *****************************************************

    @GetMapping("/getAll") //?page=&size=10
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.getAll(page,size,sort,type);
    }


    //NOT: searchByEmail **************************************************

    @GetMapping ("/searchByEmail") //http://localhost:8091/contactMessages/searchByEmail?email=aaa@bbb.com
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value= "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
                return contactMessageService.searchByEmail(email,page,size,sort,type);
    }

    //NOT: searchBySubject **************************************************

    @GetMapping ("/searchBySubject") //http://localhost:8091/contactMessages/searchBySubject?subject=deneme
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value= "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.searchBySubject(subject,page,size,sort,type);
    }

    //NOT: deleteByIdParam **************************************************
    @DeleteMapping("/deleteByIdParam") //http://localhost:8091/contactMessages/deleteByIdParam?contactMessageId=1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteById(@RequestParam(value = "contactMessageId") Long id){
        return ResponseEntity.ok(contactMessageService.deleteById(id));
    }

    //NOT: deleteByIdWithPath **********************************************
    @DeleteMapping ("/deleteById/{contactMessageId}") //http://localhost:8091/contactMessages/deleteById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    //NOT: getByIdParam ****************************************************

    @GetMapping ("/getByIdParam") //http://localhost:8091/contactMessages/getByIdParam?  +GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ContactMessage> getById(@RequestParam(value= "contactMessageId") Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    //NOT: getById **********************************************************
    //Path variable nedir??
    @GetMapping ("/getById/{contactMessageId}") //http://localhost:8091/contactMessages/getById/1  + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ContactMessage> getByIdPath(@PathVariable Long contactMessageId){
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }

    //NOT: updateById **************************************************
    @PutMapping("/updateById/{contactMessageId}") //http://localhost:8091/contactMessages/updateById/1  + PUT + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<ResponseMessage<ContactMessageResponse>> updateById(@PathVariable Long contactMessageId ,
                                                                              @RequestBody @NotNull ContactMessageUpdateRequest contactMessageUpdateRequest){
        return ResponseEntity.ok(contactMessageService.updateById(contactMessageId, contactMessageUpdateRequest));

    }

    //NOT: searchByDateBetween **************************************************

    @GetMapping("/searchByDateBetween") // //http://localhost:8091/contactMessages/searchByDateBetween?beginDate=2023-11-13&endDate=2023-11-13
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<ContactMessage>> searchByDateBetween(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString){
        List<ContactMessage>contactMessages = contactMessageService.searchByDateBetween(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }

    // NOT: searchByTimeBetween **************************************************

    @GetMapping("/searchByTimeBetween") // //http://localhost:8091/contactMessages/searchByTimeBetween?
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<ContactMessage>> searchByTimeBetween(
            @RequestParam(value = "startHour") String startHourString,
            @RequestParam(value = "startMinute") String startMinuteString,
            @RequestParam(value = "endHour") String endHourString,
            @RequestParam(value = "endMinute") String endMinuteString){
        List<ContactMessage>contactMessages = contactMessageService.searchByTimeBetween(startHourString, startMinuteString, endHourString, endMinuteString);
        return ResponseEntity.ok(contactMessages);
    }
}
