package com.blueteam.service;

import com.blueteam.bean.Account;
import com.blueteam.mapper.AccountMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    AccountMapper accountMapper;

    // fake login
    // test username admin, password admin
    public Account login(String username, String password) {
        String res = ("admin".equals(username) && "admin".equals(password)) ? "success" : "fail";
        Account account = accountMapper.selectByUsernameAndPassword(username, password);
        return account;
    }

    public int register(String username, String password, String email) {
        Account record = new Account(username, password, email);
        int res = accountMapper.insert(record);
        return res;
    }

}
