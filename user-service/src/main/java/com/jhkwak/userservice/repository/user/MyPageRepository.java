package com.jhkwak.userservice.repository.user;

import com.jhkwak.userservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPageRepository extends JpaRepository<User, Long> {

}
