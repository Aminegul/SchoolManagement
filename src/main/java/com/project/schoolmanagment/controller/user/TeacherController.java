package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController //RestController, @Controller ve @ResponseBody'nin bir kombinasyonundan oluşan bir stereotype
@RequestMapping ("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;


    //NOT: saveTeacher()**************************************************

    @PostMapping("/save") // http://localhost:8091/teacher/save  +POST +JSON
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<TeacherResponse>> saveTeacher(@RequestBody @Valid TeacherRequest teacherRequest) {
        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }
}
