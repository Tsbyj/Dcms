package com.dcms.pojo.login;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/1/30 0030 20:19
 * Description:
 **/
@Data
public class LoginInfo {
    private String userId;      //登录人ID
    private String username;    //登录人
    private String password;    //密码
    private String verity;      //验证码
    private Integer userSort;   //类别：客户，管理员，医生
}
