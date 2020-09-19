package com.dcms.pojo.doc;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/1 0001 11:27
 * Description:
 **/
@Data
public class Doctor {
    private String docId;   //医生ID
    private String docName; //姓名
    private String cid;    //身份证
    private String docSex;  //性别
    private Integer docAge;  //年龄
    private String nation;  //民族
    private Integer workYear;    //工作年限
    private String politicStatus;   //政治面貌
    private String workKind;   //工作类型
    private String docTitle;    //职称
    private String education;   //学历
    private String docIntroduce;    //个人简介
}
