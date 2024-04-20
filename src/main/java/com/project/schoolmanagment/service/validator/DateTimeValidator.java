package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.contactmessage.exception.ConflictException;
import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    public void checkLessonPrograms(Set<LessonProgram> existLessonPrograms,
                                    Set<LessonProgram> lessonProgramRequest){ //LP1 LP2 LP3

        if (existLessonPrograms.isEmpty() && lessonProgramRequest.size()>1) {
            // !!! talep edilen lesson programlar içinde çakışma var mı??    A
            checkDuplicateLessonPrograms(lessonProgramRequest);

        } else {
            // !!! talep edilen LessonProgramlar içinde çakışan var mı??     A
            checkDuplicateLessonPrograms(lessonProgramRequest);

            // !!! talep edilen ile mevcutta çakışma var mı??                B
            checkDuplicateLessonPrograms(existLessonPrograms,lessonProgramRequest);

        }


    }

    public void checkDuplicateLessonPrograms(Set<LessonProgram>lessonPrograms) {
        Set<String> uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for (LessonProgram lessonProgram : lessonPrograms) {
            String lessonProgramDay = lessonProgram.getDay().name();
            //karşılaştırılan leesonProgramlar aynı günde ise
            if (uniqueLessonProgramDays.contains(lessonProgramDay)) {
                //başlama saatine göre kontrol
                for (LocalTime startTime : existingLessonProgramStartTimes) {
                    //başlama saati eşit ise
                    if (lessonProgram.getStartTime().equals(startTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    // kıyaslanan LessonProgramının başlama saati eklenmek istenen dersProgramının başlama ve bitiş saatleri arasında mı?
                    if (lessonProgram.getStartTime().isBefore(startTime) && lessonProgram.getEndTime().isAfter(startTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }

                //bitiş saatine göre kontrol
                for (LocalTime stopTime : existingLessonProgramStopTimes) {
                    if (lessonProgram.getStartTime().isBefore(stopTime) && lessonProgram.getEndTime().isAfter(stopTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
            }
            uniqueLessonProgramDays.add(lessonProgramDay);
            existingLessonProgramStartTimes.add(lessonProgram.getStartTime());
            existingLessonProgramStopTimes.add(lessonProgram.getEndTime());
        }

    }


    private void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram,
                                              Set<LessonProgram> lessonProgramRequest){

        for (LessonProgram requestLessonProgram: lessonProgramRequest) {


            String requestLessonProgramDay =requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getEndTime();

            if (existLessonProgram.stream()
                    .anyMatch(lessonProgram -> lessonProgram.getDay().name().equals(requestLessonProgram)
                                && (lessonProgram.getStartTime().equals(requestLessonProgram)
                                || (lessonProgram.getStartTime().isBefore(requestStart) && lessonProgram.getEndTime().isAfter(requestStart))
                                || (lessonProgram.getStartTime().isBefore(requestStop) && lessonProgram.getEndTime().isAfter(requestStop))
                                || (lessonProgram.getStartTime().isAfter(requestStart) && lessonProgram.getEndTime().isBefore(requestStop))))
            ){
                throw new ConflictException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }

}

