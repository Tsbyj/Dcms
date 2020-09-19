package com.dcms.pojo.login;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/14 0014 22:41
 * Description:登录信息记录
 **/
@Data
public class LoginCount {
    private String loginId;     //登录编号：有登录日期
    private String loginData;   //登录日期
    private String username;    //登录人
    private String userId;      //登录ID
    private Integer userSort;   //登录人分类：客户，医生，管理员
}
