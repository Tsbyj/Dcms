package com.dcms.repository;

import com.dcms.pojo.login.LoginCount;

import java.util.List;

public interface LoginCountRepository {
    List<LoginCount> findAll();
    List<LoginCount> findByUserId(String userId);
    Integer save(LoginCount loginCount);
    Integer deleteById(String loginId);
    Integer getCount(String loginIdPrefix);
    List<LoginCount> getWeekData(String beginDate, String endDate);
}
