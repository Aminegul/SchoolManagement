package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameEquals(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName =:roleName")
    Page<User> findByUserByRole(String roleName, Pageable pageable);

    List<User> getUserByNameContaining(String name); // Derived methods, derived query

    @Query(value = "SELECT COUNT(u) FROM User u WHERE u.userRole.roleType= ?1")
    long countAdmin(RoleType roleType);

}

