package com.project.schoolmanagment.controller.businnes;

import com.project.schoolmanagment.payload.request.businnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.businnes.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/educationTerms")
@RequiredArgsConstructor
public class EducationTermController {
    private final EducationTermService educationTermService;

    // NOT: save() **************************************
    @PostMapping("save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseMessage<EducationTermResponse> saveEducationTerm(@RequestBody @Valid EducationTermRequest educationTermRequest){

        return educationTermService.saveEducationTerm(educationTermRequest);
    }

    // NOT: getById() *****************************************
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER')")
    public EducationTermResponse getEducationTermById (@PathVariable Long id){
        return educationTermService.getEducationTermResponseById(id);
    }

    // NOT: getAll() *****************************************

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public List<EducationTermResponse> getAllEducationTerms(){

        return educationTermService.getAllEducationTerms();
    }

    // NOT: getAllWithPage() *****************************************
    @GetMapping("/getAllEducationTermsByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public Page<EducationTermResponse> getAllEducationTermsByPage(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "startDate") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type){
        return educationTermService.getAllEducationTermsByPage(page,size,sort,type);
    }

    // NOT: deleteById() *****************************************
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public ResponseMessage deleteEducationTerm(@PathVariable Long id){

        return educationTermService.deleteById(id);
    }

    // NOT: updateById() *****************************************

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<EducationTermResponse> updateEducationTerm(@PathVariable Long id,
                                                                      @RequestBody @Valid EducationTermRequest educationTermRequest){
        return educationTermService.updateEducationTerm(id,educationTermRequest);
    }





}
