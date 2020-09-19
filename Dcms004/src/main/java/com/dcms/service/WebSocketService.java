package com.dcms.service;

import com.dcms.pojo.other.SocketMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/2/22 0022 11:46
 * Description:
 **/
@ServerEndpoint(value = "/websocket/{nickId}/{nickname}")
@Component
public class WebSocketService {
    //    单聊,不会重复创建map对象
    private static Map<String, Session> userMap = new HashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(@PathParam("nickname") String nickname, @PathParam("nickId") String nickId, Session session) {
        System.out.println("-----------WebSocketService onOpen-------------");
        this.session = session;
        // 将医生与客户session分类存储
        userMap.put(nickId,session);
        int[] num = getDocACusNum(userMap);
        System.out.println("：当前客户在线人数：" + num[1] + "：当前医生在线人数：" + num[0]);
    }

    @OnClose
    public void onClose(@PathParam("nickId") String nickid, @PathParam("nickname") String nickname) {
        userMap.remove(nickid);
        System.out.println(nickid + ":" + nickname + "--伤心绝望地离开了，当前在线人数为：-----" + userMap.size());
    }

    @OnMessage
    public void onMessage(@PathParam("nickId") String nickId, @PathParam("nickname") String nickname, Session session, String message) {
        System.out.println(nickId + ":" + nickname + "-------WebSocketService onMessage--------");
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsg socketMsg;
        try {
            // 将前端传递的数据变成对应的对象
            socketMsg = objectMapper.readValue(message, SocketMsg.class);
            Session fromSession = userMap.get(socketMsg.getFromUser());
            Session toSession = userMap.get(socketMsg.getToUser());
            String msg = socketMsg.toString();
            System.out.println(msg);
            if (socketMsg.getType() == 1) {
                //客户向医生发送消息
                if (toSession != null) {
                    toSession.getAsyncRemote().sendText(nickId + ">" + msg);
                } else {
                    fromSession.getAsyncRemote().sendText("666");
                }
            }else if(socketMsg.getType() == 2){
                // 医生回复客户消息
                if (toSession != null) {
                    toSession.getAsyncRemote().sendText(nickname + ">" + msg);
                } else if(!socketMsg.getToUser().equals("")) {
                    fromSession.getAsyncRemote().sendText("999");
                } else {
                    fromSession.getAsyncRemote().sendText("998");
                }
            } else {
                int[] nums = getDocACusNum(userMap);
                //给所有医生发送消息
                if(nums[0] > 0){
                    broadcast(nickId,nickname + "> " + msg);
                }else {
                    fromSession.getAsyncRemote().sendText("系统消息：当前没有医生在线！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(@PathParam("nickname") String nickname, Session session, Throwable t) {
        System.out.println(nickname + "连接时发生错误！");
        // 顶级异常父类
        t.printStackTrace();
    }

    private void broadcast(String nickId, String message) {
        String[] allDoc = getAllDoc(userMap);
        // 将消息发给所有在线的医生
        for(int i = 0;i < allDoc.length;i++){
            userMap.get(allDoc[i]).getAsyncRemote().sendText(message); // 异步发送消息
        }
    }
    // 获取当前在线的客户与医生数量
    private int[] getDocACusNum(Map<String, Session> map){
        int[] num = {0,0};
        for(Map.Entry<String, Session> entry : map.entrySet()){
            if(entry.getKey().contains("D")){
                num[0]++;
            }else {
                num[1]++;
            }
        }
        return num;
    }
    // 获取所有医生的sessionID
    private String[] getAllDoc(Map<String, Session> map){
        int[] num = getDocACusNum(map);
        String[] doc = new String[num[0]];
        int i = 0;
        for(Map.Entry<String, Session> entry : map.entrySet()){
            if(entry.getKey().contains("D")){
                doc[i] = entry.getKey();
                i++;
            }
        }
        return doc;
    }
}
