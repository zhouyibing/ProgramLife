package framework.mina.chat.server.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ?????
 */
public class Message {
    private String messageId;//???id
    private String sendUser;//??????
    private String receivedUser;//??????
    private Date sendTime;//???????
    private String content;//????????
    private MessageType messageType;//???????
    private String relationId;//????id

    public enum MessageType{
        PRIVATE(0),GROUP(1),CMD(2);
        private int code;
        MessageType(int code) {
            this.code = code;
        }
        private int getCode(){
            return code;
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getReceivedUser() {
        return receivedUser;
    }

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
