package com.dcms.pojo.other;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/26 0026 19:39
 * Description:
 **/
@Data
public class SocketMsg {
    public int type;            //消息类型（客户、医生、游客发送的信息分别对应：1,2,3）
    private String fromUser;    //发送者
    private String toUser;      //接受者
    private String msg;         //信息内容

    @Override
    public String toString() {
        return "{" +
                "type:" + "'" + type + "'" +
                ", fromUser:" + "'" + fromUser + "'" +
                ", toUser:" + "'" + toUser + "'" +
                ", msg:" + "'" + msg + "'" +
                '}';
    }
}
