package com.dcms.pojo.cus;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/7 0007 10:52
 * Description:
 **/
@Data
public class Customer {
    private String customerId;  //主键：客户ID:202001270001  日期与编号组合的生成方式
    private String customerName;    //名称
    private String sex; //性别
    private Integer age; //年龄
    private String cid; //身份证号 18位
    private String job; //职业
    private String phone;   //联系电话
    private String address; //家庭住址
    private String ecName;  //紧急联系人姓名
    private String ecPhone; //紧急联系人电话
    private String allergy; //过敏史
    private String attribute1;  //是否长期服用某种药物
    private String attribute2;  //女性患者是否怀孕
    private String attribute3;  //保证以上内容属实  用于病人确认
    private String attribute4;  //系统性疾病史：无/具体病症
    private String systemicDis;  //系统性疾病史
}
