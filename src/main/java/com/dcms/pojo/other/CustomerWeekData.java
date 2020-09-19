package com.dcms.pojo.other;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/15 0015 22:52
 * Description:
 **/
@Data
public class CustomerWeekData {
    private String name;
    private Integer[] data = new Integer[7];
}
