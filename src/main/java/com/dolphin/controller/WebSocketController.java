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