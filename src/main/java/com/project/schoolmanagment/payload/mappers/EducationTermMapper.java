package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.business.EducationTerm;
import com.project.schoolmanagment.payload.request.businnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class EducationTermMapper {

    //DTO -> DomainObject mapper
    public EducationTerm mapEducationTermRequestToEducationTerm(EducationTermRequest educationTermRequest){
        return EducationTerm.builder()
                .term(educationTermRequest.getTerm())
                .startDate(educationTermRequest.getStartDate())
                .endDate(educationTermRequest.getEndDate())
                .lastRegistrationDates(educationTermRequest.getLastRegistrationDate())
                .build();
    }

    // DomainObject -> DTO mapper
    public EducationTermResponse mapEducationTermToEducationTermResponse(EducationTerm educationTerm){
        return EducationTermResponse.builder()
                .id(educationTerm.getId())
                .term(educationTerm.getTerm())
                .startDate(educationTerm.getStartDate())
                .endDate(educationTerm.getEndDate())
                .lastRegistrationDate(educationTerm.getLastRegistrationDates())
                .build();
    }

    public EducationTerm mapEducationTermRequestToEducationTermForUpdate(Long id,EducationTermRequest educationTermRequest){
        EducationTerm term =  mapEducationTermRequestToEducationTerm(educationTermRequest);
        term.setId(id);
        return term;
    }

}
