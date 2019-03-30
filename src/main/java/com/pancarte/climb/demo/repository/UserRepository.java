package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    //User findUserForConn(String email,String password);
}
