package com.project.schoolmanagment.payload.response.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
// @AllArgsConstructor neden yazmıyoruz
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherResponse extends BaseUserResponse { //DTO claası burası

    private Set<LessonProgram> lessonPrograms;

    private Boolean isAdvisorTeacher;

}
