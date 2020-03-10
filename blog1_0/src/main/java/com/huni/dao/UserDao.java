package com.huni.dao;

import com.huni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long>{
    User findByNameAndPassword(String username,String password);
}