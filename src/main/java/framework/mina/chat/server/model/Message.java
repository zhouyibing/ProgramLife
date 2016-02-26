package framework.mina.chat.server.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ?????
 */
@Data
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
}
