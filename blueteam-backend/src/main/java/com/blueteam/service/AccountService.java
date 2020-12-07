package com.blueteam.service;

import com.blueteam.bean.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    // fake login
    // test username admin, password admin
    public String login(String username, String password) {
        String res = ("admin".equals(username) && "admin".equals(password)) ? "success" : "fail";
        return res;
    }

    public Account register(String username, String password, String email) {
        Account res = new Account(username, password, email);
        return res;
    }

}
