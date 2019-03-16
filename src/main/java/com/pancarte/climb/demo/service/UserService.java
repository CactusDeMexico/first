package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.User;

public interface UserService {

    public User findUserByEmail(String email);

    public void saveUser(User user);
}
