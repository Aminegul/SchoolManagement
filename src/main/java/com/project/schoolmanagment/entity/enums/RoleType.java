package com.project.schoolmanagment.entity.enums;

//haftanın günleri gibi değişme ihtimali olmayan roller için enum kullanırız
public enum RoleType {

    ADMIN("Admin"),
    TEACHER("Teacher"),
    STUDENT("Student"),
    MANAGER("Dean"),
    ASSISTANT_MANAGER("Vice Dean");

    public final String name;

    //constructor yapmazsak kızarır
    //Java'da constructor (Yapıcı) metotlar , nesneleri başlatmak için kullanılan özel bir metottur.
    // Contructor , bir sınıfın bir nesnesi oluşturulduğunda çağrılır.
    // Nesne nitelikleri için başlangıç değerini ayarlamak için kullanılabilir.
    RoleType(String name) {

        this.name = name;
    }

    //Getter
    public String getName() {

        return name;
    }


}
