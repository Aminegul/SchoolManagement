package com.project.schoolmanagment.repository.businnes;

import com.project.schoolmanagment.entity.business.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {

    @Query("SELECT l FROM LessonProgram l WHERE l.id IN :myProperty")
    Set<LessonProgram> getLessonProgramByLessonProgramIdList(Set<Long> myProperty);



}
