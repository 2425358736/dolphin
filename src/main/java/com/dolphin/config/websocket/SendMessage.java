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