package com.dcms.pojo.other;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/15 0015 17:28
 * Description:
 **/
@Data
public class CustomerLoginData {
    private String[] name = {"0-5","5-10","10-15","15-20","20-25","25-30","30-35","35-40","40-45","45-50","50-55","55-60","60以上"};
    private List<Integer> data;
}
