package com.jhkwak.userservice.repository.user;

import com.jhkwak.userservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailVerificationTokenAndEmailVerifiedStatus(String token, boolean EmailVerifiedStatus);
    Optional<User> findByIdAndPassword(Long info, String password);
}
