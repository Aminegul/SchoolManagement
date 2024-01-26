package com.project.schoolmanagment.entity.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee;

    private Double midtermExam;

    private Double finalExam;

    private Double examAverage;

    private String infoNote;

    @Enumerated (EnumType.STRING)
    private Note letterGrade;

    @ManyToOne
    @JsonIgnore
    private User teacher;

    @ManyToOne
    @JsonIgnore
    private User student;

    @ManyToOne
    private Lesson lesson;

    //NOT: StudentInfo

    @OneToOne
    private EducationTerm educationTerm;


}
