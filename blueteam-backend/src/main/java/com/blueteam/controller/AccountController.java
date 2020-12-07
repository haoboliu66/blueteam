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
        return accountService.login(username, password);
    }

    @PostMapping("/register")
    public String register(String username, String password, String email) {
        log.info("username: " + username);
        log.info("password: " + password);
        log.info("email: " + email);
        Account account = new Account(username, password, email);
        return "register success";
    }

//    @RequestMapping("/validateUsername")
//    public String validate(@RequestParam("username") String username) {
//        System.out.println("validate: " + username);
//        Account account = accountService.selectByUsername(username);
//        ReturnObject returnObject = new ReturnObject();
//        if (account != null) {
//            returnObject = new ReturnObject().setCode(-1).setMessage("userName exists");
//        }
//        return JSONObject.toJSONString(returnObject);
//    }
//
//

    @GetMapping("/logout")
    public void logOut(HttpSession session) {
        log.info("log out: ");
        session.invalidate();
    }


}
