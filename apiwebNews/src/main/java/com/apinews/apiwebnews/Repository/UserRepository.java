package com.apinews.apiwebnews.Repository;

import com.apinews.apiwebnews.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username); // Thêm dòng này
    Optional<User> findByResetToken(String resetToken);

}
