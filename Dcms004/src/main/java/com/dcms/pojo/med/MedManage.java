package com.dcms.pojo.med;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/1 0001 19:56
 * Description:
 **/
@Data
public class MedManage {
    private String produceNum;      //产品批号(primary key)
    private String medicineId;      //药品编号
    private String specification;   //规格
    private String wrap;            //包装
    private String produceDate;     //生产日期
    private String expirationDate;  //保质期
    private String toDate;          //到期时间
    private String medAddress;      //生产地址
    private Integer medNumber;      //当前库存量
    private String medFactory;      //生产厂家
    private String medTime;         //当前时间(primary key)
    private Double price;           //单价
    private Double cost;            //成本
    /*以下属性不与数据库匹配*/
    private String medName;      //药品名称
}
