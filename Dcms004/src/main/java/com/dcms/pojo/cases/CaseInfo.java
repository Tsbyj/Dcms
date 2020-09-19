package com.dcms.pojo.cases;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/1/28 0028 19:17
 * Description:
 **/
@Data
public class CaseInfo {
    private String caseId;  //病例ID
    private String customerId;  //客户ID
    private String doctorId;  //医生ID
    private String diagnoseTime;    //诊断日期
    private String selfReported;    //主诉症状 String
    private String pastHistory; //既往史 String
    private String diagnose;    //诊断
    private String diffDiagnosis;   //鉴别诊断 String
    private String treatmentPlan;   //治疗计划 String
    private String advice;  //医嘱
    private String recheckTime; //复诊时间
    private Integer trResult;    //治疗周期（新增时赋值1，每次复诊，+1）
    private Integer status;      //状态：判断该对象是新添加还是复诊，1为新增，2为复诊，3为治疗结束
    /*以下不与数据库匹配*/
    private String medMess;      //治疗需要的药品批号与数量，需要切割处理
    private Integer revisitNum; // 复诊次数
    /*private String PresentIllness;  //现病史*/
}
