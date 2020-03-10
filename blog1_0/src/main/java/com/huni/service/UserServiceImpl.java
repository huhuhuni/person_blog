package com.huni.service;

import com.huni.dao.UserDao;
import com.huni.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public User checkUser(String username, String password) {
           User user=  userDao.findByNameAndPassword(username,password);
           return user;

    }
}