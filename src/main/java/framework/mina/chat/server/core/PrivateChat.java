package framework.mina.chat.server.core;

import framework.mina.chat.server.model.Message;
import framework.quartz.util.UUID;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
public class PrivateChat extends Chat{
    private static PrivateChat privateChat;

    private PrivateChat() {
    }

    public static PrivateChat getPrivateChat(){
        if(null==privateChat){
            synchronized (PrivateChat.class){
                if(null==privateChat)
                    privateChat=new PrivateChat();
                privateChat.start();
            }
        }
        return privateChat;
    }

    public void sendMessage(String sendClient,String recivedClient,String content){
       Message message = new Message();
        message.setMessageId(UUID.randomUUID().toString());
        message.setContent(content);
        message.setMessageType(Message.MessageType.PRIVATE);
        message.setSendUser(sendClient);
        message.setReceivedUser(recivedClient);
        putMessage(message);
    }

}
