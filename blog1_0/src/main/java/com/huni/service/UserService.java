package com.huni.service;

import com.huni.entity.User;

public interface UserService {
    User checkUser(String username, String password);
}