package com.blueteam.controller;


import com.alibaba.fastjson.JSONObject;
import com.blueteam.bean.Account;
import com.blueteam.service.AccountService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class AccountController {

    Log log = LogFactory.getLog(AccountController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password) {
        log.info("username: " + username);
        log.info("password: " + password);
        Account account = accountService.login(username, password);
        return JSONObject.toJSONString(account);
    }

    @PostMapping("/register")
    public String register(String username, String password, String email) {
        log.info("username: " + username);
        log.info("password: " + password);
        log.info("email: " + email);
        int res = accountService.register(username, password, email);
        if(res > 0){
            return JSONObject.toJSONString(new String("register success"));
        }
        return JSONObject.toJSONString(new String("register fail"));
    }



}
