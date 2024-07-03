package com.jhkwak.userservice.repository;

import com.jhkwak.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository extends JpaRepository<User, Long> {

}
