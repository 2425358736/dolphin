### [WebSocket（html前端）参考文档](http://note.youdao.com/)
### WebSocket 是 HTML5 开始提供的一种在单个 TCP 连接上进行全双工通讯的协议。

### springboot本身也集成了websocket 服务端开发包

## 集成
1. pom.xml引用 websocket开发包

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

2. 新建websocket配置类 WebSocketConfig.java

```
package com.dolphin.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * WebSocketConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/14
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
```
3. 新建发送消息实体类  SendMessage.java

```
package com.dolphin.config.websocket;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SendMessage
 *
 * @author 刘志强
 * @created Create Time: 2019/2/14
 */
public class SendMessage {
    // 发送人id
    private Long sendId;
    // 接收人id
    private Long userId;
    // 发送的消息
    private String message;

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    @Override
    public String toString() {
        return sendId + ":" + userId + ":" + message;
    }
}
```


4. 新建websocket服务端点类 MyWebSocket.java

```
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
```

5. 新建HTML页面 webSocket.ftl

```
<html xmlns="http://www.w3.org/1999/html">
    <script>
        let ws;
        function open () {
            if ("WebSocket" in window)
            {

                // 打开一个 web socket
                ws = new WebSocket("ws://localhost:6533/websocket?${userId}");

                ws.onopen = function()
                {
                    alert("websocket连接成功");
                }
            }
            else
            {
                // 浏览器不支持 WebSocket
                alert("您的浏览器不支持 WebSocket!");
            }
        }

        open ();

        ws.onmessage = function (evt)
        {
            var received_msg = evt.data;
            document.getElementById("content").innerHTML = document.getElementById("content").innerHTML + "<span>" + received_msg + "</span></br>";
            //alert("接受到来自服务端消息：" + received_msg);
        };

        ws.onclose = function()
        {
            alert("WebSocket连接关闭");
        };

        ws.onerror = function()
        {
            alert("WebSocket连接异常，发起重新连接");
            open ();
        };
        send = () => {
            const userId  = document.getElementById("userId").value;
            const message  = document.getElementById("message").value;
            if (message === '' && userId === '') {
                alert("消息和接收人id不可以为空");
            } else {
                ws.send(${userId!""} + ":" + userId + ":" + message);
            }
        }
        ajaxSend = () => {
            const userId  = document.getElementById("userId").value;
            const message  = document.getElementById("message").value;
            if (message === '' && userId === '') {
                alert("消息和接收人id不可以为空");
            } else {
                const httpRequest = new XMLHttpRequest();//第一步：建立所需的对象
                httpRequest.open('GET', 'http://localhost:6533/webSocket/send?sendId=' + ${userId!""} + '&userId='+ userId + "&message=" + message, true);//第二步：打开连接  将请求参数写在url中  ps:"./Ptest.php?name=test&nameone=testone"
                httpRequest.send();//第三步：发送请求  将请求参数写在URL中
                /**
                 * 获取数据后的处理程序
                 */
                httpRequest.onreadystatechange = function () {
                    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                        const json = httpRequest.responseText;//获取到json字符串，还需解析
                        console.log(json);
                        // alert(json);
                    }
                };
            }
        }
    </script>
    <body>
        <h1>作者：${作者!""}</h1>
        <h3>当前登录人userId： ${userId!""}</h3>
        <div>
            <h3>聊天内容</h3>
            <div id="content" />
        </div>
        <span>
            接收人id：<input id="userId" />
            发送的消息：<input id="message" />
            <button onclick="send()">js发送</button>
        </span>
        <span>
            <button onclick="ajaxSend()">ajax发送</button>
    </body>
</html>
```

6. 新建测试controller WebSocketController.java

```
package com.dolphin.controller;

import com.dolphin.config.websocket.MyWebSocket;
import com.dolphin.config.websocket.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * WebSocket
 *
 * @author 刘志强
 * @created Create Time: 2019/2/14
 */
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {
    @Autowired
    MyWebSocket myWebSocket;

    static Long userId = new Long("0");
    @GetMapping("index")
    public ModelAndView index(ModelMap modelMap){
        modelMap.put("作者","刘志强");
        modelMap.put("userId", userId++);
        return new ModelAndView("/freemarker/webSocket", modelMap);
    }

    @GetMapping("send")
    @ResponseBody
    public Map<String,Object> send(SendMessage sendMessage){
       Map<String,Object> map = new HashMap<>();
        myWebSocket.sendMessage(sendMessage);
        map.put("code",200);
        map.put("message","发送成功");
        return map;
    }

}
```
7. 访问 http://localhost:6533/webSocket/index

8. 本文中前端采用的freemarker模板引擎，没用过得朋友可以参考 [springboot集成freemarker](http://note.youdao.com/noteshare?id=acca2206d855c266211f1e5596154911&sub=9BECC963C90A48C29CD8B7ACC9B03234) 配置。也可以替换为别的模板引擎。

9. 设计思路。 前端连接socket时携带id参数，webSocket连接成功时取出id和当前webSocket sesson作为map存储在集合中。当发送时，从发送对象中取出接受人id,遍历集合取出接收人的webSocket对象，然后发送。






