package com.dcms.pojo.med;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/1 0001 18:02
 * Description:
 **/
@Data
public class Medicine {
    private String medicineId;      //药品编号
    private String medName;         //药品名称
    private String medType;         //药品类型：C1内服、C2外用、B1保健品、S手术专用
    private String ingredient;      //成分
    private String medCharacter;    //性状
    private String majorFunction;   //功能主治
    private String useMethod;       //用法用量
    private String unEffect;        //不良反应
    private String taboo;           //禁忌
    private String notice;          //注意事项
    private String store;           //贮藏
    private Integer medNum;          //该类药全部库存量，所有批次数量相加
    private Integer medMCount;       //该类药共进了多少批
    private String medTime;         //库存最新记录日期
}
