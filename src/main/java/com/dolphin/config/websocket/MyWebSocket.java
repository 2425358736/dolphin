package com.dolphin.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * MyWebSocket
 *
 * @author 刘志强
 * @created Create Time: 2019/2/14
 */
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<Map<String, MyWebSocket>> webSocketSet = new CopyOnWriteArraySet<Map<String, MyWebSocket>>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocket.class);


    public void sendMessage(SendMessage sendMessage) {
        onMessage(sendMessage.toString());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public static void onMessage(String message) {
        logger.info("来自客户端的消息:" + message);
        SendMessage sendMessage = new SendMessage();
        String[] str = message.split(":");
        boolean on = true;
        if (StringUtils.equals(str[0], "null")) {
            on = false;
            logger.error("发送人为空");
        } else {
            sendMessage.setSendId(new Long(str[0]));
        }
        if (StringUtils.equals(str[1], "null")) {
            on = false;
            logger.error("接收人为空");
        } else {
            sendMessage.setUserId(new Long(str[1]));
        }
        if (StringUtils.equals(str[2], "null")) {
            on = false;
            logger.error("消息是空消息");
        } else {
            sendMessage.setMessage(str[2]);
        }

        if (on) {
            //群发消息
            // 发送人的MyWebSocket对象
            MyWebSocket websockettestSend = new MyWebSocket();
            boolean onoff = true;
            for (Map<String, MyWebSocket> item : webSocketSet) {
                String userId = "";
                MyWebSocket websockettest = new MyWebSocket();

                for (String key : item.keySet()) {
                    userId = key;
                    websockettest = item.get(key);
                }
                if (sendMessage.getUserId() != null) {
                    if (StringUtils.equals(sendMessage.getUserId().toString(), userId)) {
                        onoff = false;
                        try {
                            websockettest.sendMessage("发送者id:" + sendMessage.getSendId() + "=======发送内容：" + sendMessage.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 获取发送人的MyWebSocket对象
                if (sendMessage.getSendId() != null) {
                    if (StringUtils.equals(sendMessage.getSendId().toString(), userId)) {
                        websockettestSend = websockettest;
                    }
                }
            }
            // 未找到用户给发送人推送消息
            if (onoff) {
                try {
                    websockettestSend.sendMessage("此用户未上线");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (Map<String, MyWebSocket> item : webSocketSet) {
            MyWebSocket websockettest = new MyWebSocket();
            for (String key : item.keySet()) {

                websockettest = item.get(key);
            }
            try {
                websockettest.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Map<String, MyWebSocket> map = new HashMap<String, MyWebSocket>();
        map.put(session.getQueryString(), this);
        webSocketSet.add(map);     //加入set中
        addOnlineCount();           //在线数加1
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        for (Map<String, MyWebSocket> item : webSocketSet) {
            for (String key : item.keySet()) {
                if (item.get(key) == this) {
                    webSocketSet.remove(item);
                }
            }
        }
        //从set中删除
        subOnlineCount();           //在线数减1
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //System.out.println("发生错误");
        logger.info(error.getMessage());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}