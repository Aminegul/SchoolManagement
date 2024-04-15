package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.businnes.ChooseLessonProgramWithId;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping ("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // NOT: saveStudent()*************************************************

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }


    // NOT: updateStudentForStudents()*************************************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PatchMapping("/update")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentRequestWithoutPassword studentRequestWithoutPassword,
                                                HttpServletRequest request){
        return studentService.updateStudent(studentRequestWithoutPassword, request);
    }

    // NOT: updateStudent()******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASISTANT_MANAGER')")
    @PutMapping("/update/{userId}")
    public ResponseMessage<StudentResponse> updateStudentForManagers(@PathVariable Long userId,
                                                                     @RequestBody @Valid StudentRequest studentRequest){
        return studentService.updateStudentForManagers(userId, studentRequest);

    }


    // NOT: addLessonProgramToStudentLessonProgram ********************************

    // !!! Student kendine lessonProgram ekleyecek

    @PostMapping("/addLessonProgramToStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> addLessonProgram(HttpServletRequest request, // öğrencinin kim olduğunu bilmemiz için
                                                             @RequestBody @Valid ChooseLessonProgramWithId chooseLessonProgramWithId){
       String userName = (String) request.getAttribute("username");
       return studentService.addLessonProgramToStudent(userName, chooseLessonProgramWithId);
    }

    // NOT: changeActiveStatusOfStudent **********************************************
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASISTANT_MANAGER')")
    @PatchMapping ("/changedStatus")
    public ResponseMessage changeStatusOfStudent(@RequestParam Long studentId, @RequestParam boolean status){
        return studentService.changeStatusOfStudent(studentId,status);
    }



}
