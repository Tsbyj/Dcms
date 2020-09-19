package com.dcms.repository;

import com.dcms.pojo.login.LoginInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository {
    LoginInfo findByName(String username);

    LoginInfo findById(String userId);

    Integer save(LoginInfo loginInfo);

    // 更新账号密码
    Integer update(LoginInfo loginInfo);

    Integer deleteById(String userId);
}
