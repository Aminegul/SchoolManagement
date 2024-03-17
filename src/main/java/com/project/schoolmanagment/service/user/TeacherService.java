package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //Repository katmanıyla injektion işlemi yapılacağı için ve Class içinde final ve NonNull olan değişkenleri parametre olarak alan bir constructor oluşturur.
public class TeacherService {
    private final UserRepository userRepository;

    public Object saveTeacher(TeacherRequest teacherRequest) {

    }
}
