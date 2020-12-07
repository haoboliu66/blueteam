package com.blueteam.mapper;

import com.blueteam.bean.Account;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountMapper extends MyBatisBaseDao<Account, Integer> {

    Account selectByUsernameAndPassword(String username, String password);

    Account selectByUsername(String username);

}