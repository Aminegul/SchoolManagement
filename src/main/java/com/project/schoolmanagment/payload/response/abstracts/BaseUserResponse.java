package com.project.schoolmanagment.payload.response.abstracts;

import com.project.schoolmanagment.entity.business.LessonProgram;
import com.project.schoolmanagment.entity.enums.Gender;
import com.project.schoolmanagment.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder // @Builder -bu builer hiçbir zaman kullanılmayack çünkü burası abstract class ama bu fieldlar bize lazım
//SuperBuilder - Diğer classların parentı olduğunu anlıyor ve child classları setlemeyi sağlıyor
public abstract class BaseUserResponse { //bu sınıf ne işe yarar? //DTO classı - abstract claas olduğu için
                                        // BaseUserResponse türünde bir nesne hiç bir zaman oluşturmucaz

    private Long userId;

    private String username;

    private String ssn;

    private String name;

    private String surname;

    private LocalDate birthDay;

    private String birthPlace;

    private Gender gender;

    private String phoneNumber;

    private String email;

    //private Long advisorTeacherId; //opsiyonel -- sadece techer için geçerli

    private String userRole;

    // private Set<LessonProgram> lessonProgramList; //opsiyonel - hem teacher hem student için

}
