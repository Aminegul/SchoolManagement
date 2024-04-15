package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.contactmessage.exception.ResourceNotFoundException;
import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.businnes.LessonProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LessonProgramService {
    private final LessonProgramRepository lessonProgramRepository;

    public Set<LessonProgram> getLessonProgramById(Set<Long> lessonIdSet){
        Set<LessonProgram> lessonPrograms = lessonProgramRepository.getLessonProgramByLessonProgramIdList(lessonIdSet);

        if (lessonPrograms.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_WITHOUT_ID_MESSAGE);
        }

        return lessonPrograms;
    }
}
