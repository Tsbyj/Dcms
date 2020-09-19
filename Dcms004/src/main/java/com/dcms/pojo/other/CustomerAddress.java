package com.dcms.pojo.other;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/15 0015 11:18
 * Description:
 **/
@Data
public class CustomerAddress{
    private Integer value;  // 该地点数量
    private String name;    // 地区名

    public CustomerAddress() {
    }

    public CustomerAddress(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public CustomerAddress clone() throws CloneNotSupportedException {
        return new CustomerAddress(this.value, this.name);
    }
}
