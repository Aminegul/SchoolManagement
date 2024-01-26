package com.project.schoolmanagment.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.entity.business.Meet;
import com.project.schoolmanagment.entity.business.StudentInfo;
import com.project.schoolmanagment.entity.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "t_user")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder (toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //????????????????????????????????????
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private Boolean builtIn; //sistemde 2 admin var ve kullanıcıları silme yetkisine sahipler biri diğerini sildi ve kendini sildi. bazı userların silinemez özelliği olmalı bunu kimse silemeyecek.built in true ise silinemez olacak.

    private String motherName;// studentlar için

    private String fatherName;// studentlar için

    private int studentNumber; // studentlar için

    private boolean isActive; // studentlar için

    private Boolean isAdvisor; // teacherlar için

    private Long advisorTeacherId; //bu field studentlar için eklendi

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne //bir userın bir rolle ilişkisi olsun
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

    //NOT: StudenInfo-LessonProgram_ Meet
    @OneToMany(cascade = CascadeType.REMOVE)
    //ikinci taraftan many gelmesi gerektiği için list yapıyoruz tek data yazamayız
    //user silinirse bunun student infoları da silinsin diye = cascade = CascadeType.REMOVE
    private List<StudentInfo> studentInfos; //SET OLABİLİR ??

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_lessonprogram",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id")
    )
    private Set<LessonProgram> lessonProgramList;

    @JsonIgnore
    @ManyToMany
    @JoinTable( name = "meet_student_list",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "meet_id"))
    private List<Meet>meetList;


}
