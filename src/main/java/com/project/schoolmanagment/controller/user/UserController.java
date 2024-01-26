package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.request.user.UserRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping ("/user")
@RequiredArgsConstructor

public class UserController{

    private final UserService userService;  // variable field ya da değişken
    // Dependency Injektion - Service Classın BEAN'ini oluşturmak.
    //AutoWired yapsakdık final yapmamıza gerek olmazdı.

    // NOT: saveUser()*************TEACHER VE STUDENT HARİÇ***************** (ADMİN, DEAN, ASİSTANT MENAGER)
    @PostMapping("/save/{userRole}") // http://localhost:8080/user/save/Admin + POST +JSON // postalama - kaydettiğimiz için
    @PreAuthorize("hasAnyAuthority('ADMIN')") //securityden geliyor - post mappingi sadece kimin yapabileceğini belirtir yani sadece admin yabailir.
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(@PathVariable String userRole,
                                                                  @RequestBody @Valid UserRequest userRequest){

        //saveUser - metod ismi oluyor

        // ResponseEntity<ResponseMessage<UserResponse>> -----> ResponseMessage kendimizin oluşturduğumuz ResponseEntity'nin alternatifidir.

        //  @NotNull -   @Size -  @Pattern --->bunlar validation yani doğrulama @Valid anatationu UserRequest içindeki doğrulamaları burda uyguluyor.
        //  @RequestBody anatationu "private String username;" gibi parametre olarak body gönderiyorum diyor.
        return ResponseEntity.ok(userService.saveUser(userRequest, userRole));

    }

    // NOT: getAllAdminOrDeanOrViceDean()**************************************
    @GetMapping("/gelAllUserByPage/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getUserByPage(
            @PathVariable String userRole,
            @RequestParam(value = "page", defaultValue = "0") int page, //endpointe bazı şeyler eklenmesini istiyoruz ve integer data türünde page isimli bir değişken ile maplensin diyoruz
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type //5 tane parametre oluşturduk
    ){
        //bu parametreleri service katındaki bir metoda göndermem lazım
        Page<UserResponse> adminsOrDeansOrViceDeans = userService.getUserByPage(page, size, sort, type, userRole);
        // userService içinde getUserByPage gibi bir metod vamış gibi yazıyoruz ve bu metodun argümanlarını sırayla veriyoruz = (page, size, sort, type, userRole)
        //userService katında  bu metod tetiklendiği zaman bize page yapıda içerisinde UserResponse yapıları bulunan adminlerin DTO hallerini göndermiş olacak ----> değişken=adminsOrDeansOrViceDeans
        return new ResponseEntity<>(adminsOrDeansOrViceDeans, HttpStatus.OK);
    }

    // NOT: getUserById() ****************************************************
    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BaseUserResponse>getUserById(@PathVariable Long userId){ //bu admin sistemdeki herhangi bir admini çekebilmesi lazım
        //2 id li bir user aynı zamanda student olsa bu class studentın bütün bilgilerini bana sağlar mı? Hayır.
        //burda ResponseMessage<?> yerine ne yazmamız lazım? BaseUserResponse yazarsak hepsini döndürür. Bütün rol sahiplerini döndürmemiz lazım.
        //BaseUserResponse u olması gereken rollere setleyeceğiz.
        //ya UserResponse döndecek ya StudentResponse dönecek ya da TeacherResponse dönecek
        return userService.getUserById(userId);
    }

    // NOT: deleteUser()******************************************************
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String> deleteUserById (@PathVariable Long id,
                                                  HttpServletRequest httpServletRequest){ // HttpServletRequest delete işlemini yapan kişinin kimliğini tanıyor
        //ResponseEntity<String> silinen objeyi göndermemize gerek yok zaten silindi ama silme işlemi başarıyla gerçekleştirildi gibi bi cümleyi String ile gönderebiliriz
        // HttpServletRequest?????????*
        return ResponseEntity.ok(userService.deleteUserById(id, httpServletRequest));
    }

    // NOT: updateAdminOrDeanOrViceDean**************************************
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BaseUserResponse> updateAdminOrDeanOrViceDean(@RequestBody @Valid UserRequest userRequest,
                                                                         //@RequestBody anotasyonu pathvariable ve RequestParam annotasyonu gibi kullanıcıdan veri almamızı sağlayan bir anotasyondur. Fakat diğerlerinden farklı olarak
                                                                         // kullanıcıdan obje şeklinde veri almamızı sağlar. // @RequestBody kullanıcıdan veri alıyor @Valid ise veriyi doğruluyor.
                                                                         // https://medium.com/@celilsahinozgen/veri-do%C4%9Frulama-ve-valid-anotasyonu-cc874e248ad6
                                                                         @PathVariable Long userId){ //PathVariable anotasyonu url de bulunan değişkenleri ilgili metodlara aktararak ilgili metodun işlemi yapmasını sağlamaktayız.
        return userService.updateUser(userRequest, userId);
    }

    // NOT: updateUserForUsers()********************************************
    // passwword sışındaki verileri update edicez bu yüzden @PatchMapping
    @PatchMapping ("/updateUser") // HTTP PATCH isteği, bir kaynağın kısmi bir güncelleme yapılmasını sağlar. Yani, bir kaynağın yalnızca belirli alanları değiştirilmek istendiğinde PATCH isteği kullanılır. Bu, verilerin tamamını güncelleme gereği olmadan, sadece değiştirilmesi istenen alanların değiştirilebileceği bir yöntemdir.
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')") //Student student controllerda olacak
    public ResponseEntity<String> updateUser (@RequestBody @Valid UserRequestWithoutPassword userRequestWithoutPassword,
                                              HttpServletRequest request){ // HttpServletRequest update işlemini yapan kişinin kimliğini tanıyor

        return userService.updateUserForUsers(userRequestWithoutPassword, request);// parametre burda argüman oluyor
    }

    // NOT: getByName()************************************************

    @GetMapping("/getUserByName") // http://localhost:8080/user/getUserByName?name=user1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<UserResponse> getUserByName( @RequestParam(name="name") String name ){  // @RequestParam: Bu anotasyon, bir metodun çağrılması sırasında gönderilen bir parametreyi almasını sağlar. Örneğin, bir metodun /customer/create adresiyle çağrılması sırasında gönderilen “firstName” parametresini almasını sağlamak için @RequestParam(“firstName”) String firstName anotasyonunu kullanabilirsiniz.

        return userService.getUserByName(name);

        //TODO şu dersleri alan öğrencileri getir??????????? nasıl yapılır
    }


}
