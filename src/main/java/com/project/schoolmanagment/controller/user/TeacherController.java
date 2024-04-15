package com.project.schoolmanagment.controller.user;


import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;

import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.TeacherService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    //NOT: updateTeacherById()**************************************************
    @PutMapping("/update/{userId}")  // http://localhost:8091/teacher/update/5  +PUT +JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> updateTeacherForManagers (@RequestBody @Valid TeacherRequest teacherRequest,
                                                                      @PathVariable Long userId){

        return teacherService.updateTeacherForManager(teacherRequest, userId);
    }

    //NOT: getAllStudentByAdvTeacherUserName()*************************************
    //bir rehber öğretmenin kendi öğrencilerinin tamamını getiren metod
    @GetMapping("/getAllStudentByAdvisorUsername")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse> getAllStudentByAdvisorUsername (HttpServletRequest request){ //login olan kullanıcının biligisini buradadan alıcaz

        String userName = request.getHeader("username");
        return teacherService.getAllStudentByAdvisorUsername(userName);
    }

    //TODO: AddLessonProgramToTeachersLessonProgram

    // NOT: SaveAdvisorTeacherByTeacherId()**************************************
    @PatchMapping("/saveAdvisorTeacher/{teacherId}")  // http://localhost:8091/teacher/saveAdvisorTeacher/1  +PATCH
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<UserResponse> saveAdvisorTeacher(@PathVariable Long teacherId){ //UserResponse yerine TeacherResponse da olur
        return teacherService.saveAdvisorTeacher(teacherId);
    }

    // NOT: deleteAdvisorTeacherById()******************************************
    @DeleteMapping("/deleteAdvisorTeacherById/{Id}")  // http://localhost:8091/teacher/deleteAdvisorTeacherById/2 +DELETE
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(@PathVariable Long id){
        return teacherService.deleteAdvisorTeacherById(id);
    }

    // NOT: getAllAdvisorTeacher()******************************************
    @GetMapping("/getAllAdvisorTeacher")  // http://localhost:8091
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<UserResponse> getAllAdvisorTeacher(){
        return teacherService.getAllAdvisorTeacher();
    }



}
