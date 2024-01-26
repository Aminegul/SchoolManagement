package com.project.schoolmanagment.contactmessage.service;

import com.project.schoolmanagment.contactmessage.dto.ContactMessageRequest;
import com.project.schoolmanagment.contactmessage.dto.ContactMessageResponse;
import com.project.schoolmanagment.contactmessage.dto.ContactMessageUpdateRequest;
import com.project.schoolmanagment.contactmessage.entity.ContactMessage;
import com.project.schoolmanagment.contactmessage.exception.ConflictException;
import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.contactmessage.mapper.ContactMessageMapper;
import com.project.schoolmanagment.contactmessage.message.Messages;
import com.project.schoolmanagment.contactmessage.repository.ContactMessageRepository;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    //Not: save() ************************************************

    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        ContactMessage contactMessage = contactMessageMapper.mapContactMessageRequestToContactMessage(contactMessageRequest);
        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.mapContactMessageToContactMessageResponse(savedData))
                .build();
    }

    //POJO NEDİR? JSON un Java dilindeki karşılığı Pojo classtır.

    //BU KISMI BURHANLA YAZDIK
/*    public ResponseEntity<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = contactMessageMapper.mapContactMessageRequestToContactMessage(contactMessageRequest);
        ContactMessage savedContactMessage = contactMessageRepository.save(contactMessage);
        ContactMessageResponse contactMessageResponse = contactMessageMapper.mapContactMessageToContactMessageResponse(savedContactMessage);
        return ResponseEntity.ok(contactMessageResponse);
    }*/
    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        }
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::mapContactMessageToContactMessageResponse);

    }

    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        return contactMessageRepository.findByEmailEquals(email, pageable).map(contactMessageMapper::mapContactMessageToContactMessageResponse);
    }

    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        return contactMessageRepository.findBySubjectEquals(subject, pageable).map(contactMessageMapper::mapContactMessageToContactMessageResponse);
    }

    //NOT: getById **********************************************************
    public ContactMessage getContactMessageById(Long contactMessageId) {
        return contactMessageRepository.findById(contactMessageId).orElseThrow(() ->
                new ResourceNotFoundException(Messages.NOT_FOUND_EXCEPTION));
    }

    public String deleteById(Long contactMessageId) {

        // contactMessageRepository.delete(getContactMessageById(contactMessageId));
        getContactMessageById(contactMessageId);
        contactMessageRepository.deleteById(contactMessageId);
        return Messages.CONTACT_MESSAGE_DELETE;
    }

    //NOT: updateById **************************************************
    public ResponseMessage<ContactMessageResponse> updateById(Long id, ContactMessageUpdateRequest contactMessageUpdateRequest) {
        ContactMessage contactMessage = getContactMessageById(id);

        if (contactMessageUpdateRequest.getMessage() != null) {
            contactMessage.setMessage(contactMessageUpdateRequest.getMessage());
        }

        if (contactMessageUpdateRequest.getSubject() != null) {
            contactMessage.setSubject(contactMessageUpdateRequest.getSubject());
        }

        if (contactMessageUpdateRequest.getName() != null) {
            contactMessage.setName(contactMessageUpdateRequest.getName());
        }

        if (contactMessageUpdateRequest.getEmail() != null) {
            contactMessage.setEmail(contactMessageUpdateRequest.getEmail());
        }

        contactMessage.setDateTime(LocalDateTime.now());
        contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.mapContactMessageToContactMessageResponse(contactMessage))
                .build();
    }


    public List<ContactMessage> searchByDateBetween(String beginDateString, String endDateString) {
        try {
            LocalDate beginDate = LocalDate.parse(beginDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
            return contactMessageRepository.findMessagesBetweenDates(beginDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ConflictException(Messages.WRONG_DATE_FORMAT);
        }

    }


    public List<ContactMessage> searchByTimeBetween(String startHourString, String startMinuteString,
                                                    String endHourString, String endMinuteString) {
        try {
            int startHour = Integer.parseInt(startHourString);
            int starMinute = Integer.parseInt(startMinuteString);
            int endHour = Integer.parseInt(endHourString);
            int endMinute = Integer.parseInt(endMinuteString);

            if (endHour > 23 || endHour < 0 || startHour > 23 || startHour < 0) {
                throw new ConflictException("Hour format is wrong!");
            }

            if (endMinute > 59 || endMinute < 0 || starMinute > 59 || starMinute < 0) {
                throw new ConflictException("Minute format is wrong!");
            }

            return contactMessageRepository.findMessagesBetweenTimes(startHour, starMinute, endHour, endMinute);
        } catch (NumberFormatException e) {
            throw new ConflictException(Messages.WRONG_TIME_FORMAT);
        }

    }
}
