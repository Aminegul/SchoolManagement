package com.project.schoolmanagment.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.schoolmanagment.entity.enums.Day;
import com.project.schoolmanagment.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime endTime;

    @ManyToMany
    @JoinTable(
            name="lesson_program_lesson",
            joinColumns = @JoinColumn(name="lessonprogram_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    //private Set<User> teacher;
    //private Set<User> student; bunları ayrı ayrı yazmak yerine direkt user olarak yazabiliriz
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonProgramList", fetch = FetchType.EAGER)
    private Set<User> users;

    @PreRemove//bu entity clasının bir instance si silinecekse bunu silmeden önce bunları çalıştır
    private void removeLessonProgramFromUser(){
        users.forEach(user -> user.getLessonProgramList().remove(this)); //this: bu classta instancesi tetiklensin diyoruz ama instancesi yok o yüzden this diyoruz
    }

}
