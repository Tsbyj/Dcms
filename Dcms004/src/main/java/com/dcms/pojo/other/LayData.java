package com.dcms.pojo.other;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/7 0007 18:19
 * Description:
 **/
@Data
public class LayData<T> {
//    private Integer page;   //第几页
//    private Integer limit;  //几条数据
    private Integer code;   //成功与否
    private String msg;     //数据描述
    private Integer count;  //数据总量
    private List<T> data;   //数据内容
}
