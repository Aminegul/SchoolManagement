package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.contactmessage.exception.ConflictException;
import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.entity.business.EducationTerm;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.mappers.EducationTermMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.businnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.businnes.EducationTermRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;
    private final PageableHelper pageableHelper;

    // NOT: save() **************************************
    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {
        //tarih kontrolü
        validateEducationTermDatesForRequest(educationTermRequest);
        //DTO ---> POJO dönüşümü
        EducationTerm savedEducationTerm = educationTermRepository.save(
                educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest));

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(savedEducationTerm))
                .build();
    }

    private void validateEducationTermDatesForRequest(EducationTermRequest educationTermRequest) {

        //bu metodda amacımız requestte gelen registirationDate, startDate, endDate arasındaki
        // tarih sırasına göre doğru mu setlenmiş onu kontrol etmek
        //registration, startDate den sonra olmalı
        if (educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new ResourceNotFoundException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }

        // endDate startDate ten sonra olmalı
        if (educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
            throw new BadRequestException(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }

    }

    private void validateEducationTermDates(EducationTermRequest educationTermRequest) {
        validateEducationTermDatesForRequest(educationTermRequest);
        //bir yıl içinde bir tane güz bir tane yaz dönemi olabilir
        if (educationTermRepository.existByTermAndYear(educationTermRequest.getTerm(), educationTermRequest.getStartDate().getYear())) {
            throw new ResourceNotFoundException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR);
        }

        // eklenecek educationTerm mevcuttaki educationTerm ler ile çakışmamalı

        if (educationTermRepository.findByYear(educationTermRequest.getStartDate().getYear())
                .stream()
                .anyMatch(educationTerm ->
                        (educationTerm.getStartDate().equals(educationTermRequest.getStartDate())
                        || (educationTerm.getStartDate().isBefore(educationTermRequest.getStartDate())
                            && educationTerm.getEndDate().isAfter(educationTermRequest.getStartDate()))
                        ||(educationTerm.getStartDate().isBefore(educationTermRequest.getEndDate())
                            && educationTerm.getEndDate().isAfter(educationTermRequest.getEndDate()))
                        ||(educationTerm.getStartDate().isAfter(educationTermRequest.getStartDate())
                            && educationTerm.getEndDate().isBefore(educationTermRequest.getEndDate()))))){

                throw new BadRequestException(ErrorMessages.EDUCATION_TERM_CONFLICT_MESSAGE);
        }

    }

    // NOT: getById() *****************************************
    public EducationTermResponse getEducationTermResponseById(Long id) {
        EducationTerm term = isEducationTermExist(id);
        return  educationTermMapper.mapEducationTermToEducationTermResponse(term);
    }

    public EducationTerm isEducationTermExist(Long id){
        return educationTermRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id)));
    }

    // NOT: getAll() *****************************************
    public List<EducationTermResponse> getAllEducationTerms() {
        return educationTermRepository.findAll()
                .stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponse)
                .collect(Collectors.toList());
    }

    // NOT: getAllWithPage() *****************************************
    public Page<EducationTermResponse> getAllEducationTermsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return educationTermRepository.findAll(pageable)
                .map(educationTermMapper::mapEducationTermToEducationTermResponse);
    }

    // NOT: deleteById() *****************************************
    public ResponseMessage deleteById(Long id) {
        isEducationTermExist(id);
        educationTermRepository.deleteById(id);
        //silinen education termlere ait olan lessonProgramlar ne olacak ??? Cascade Type den dolayı herhangi bir logic yapmaya gerek yok
        return ResponseMessage.builder()
                .message(SuccessMessages.EDUCATION_TERM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    // NOT: updateById() *****************************************
    public ResponseMessage<EducationTermResponse> updateEducationTerm(Long id, EducationTermRequest educationTermRequest) {
        isEducationTermExist(id);
        //check the expected dates are correct
        validateEducationTermDatesForRequest(educationTermRequest);

        EducationTerm updatedEducationTerm = educationTermRepository.save(educationTermMapper.mapEducationTermRequestToEducationTermForUpdate(id,educationTermRequest));

        return ResponseMessage.<EducationTermResponse>
                        builder()
                .message(SuccessMessages.EDUCATION_TERM_UPDATE)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(updatedEducationTerm))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
