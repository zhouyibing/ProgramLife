package framework.mina.chat.server.service;

import framework.mina.chat.server.core.ChatRoom;
import framework.mina.chat.server.core.PrivateChat;

/**
 * Created by Zhou Yibing on 2016/2/25.
 */
public class ChatService implements IChatService{
    private ChatRoom chatRoom;
    private PrivateChat privateChat;

    public ChatService() {
        chatRoom = ChatRoom.getChatRoom();
        privateChat = PrivateChat.getPrivateChat();
    }

    @Override
    public void sendRoomMessage(String sendUserId,String roomId,String content) {
        chatRoom.sendMessage(sendUserId,roomId,content);
    }

    @Override
    public void send2MyFriend(String sendUserId,String friendId,String content) {
        privateChat.sendMessage(sendUserId,friendId,content);
    }

    @Override
    public void close(){
        chatRoom.close();
        privateChat.close();
    }
}
