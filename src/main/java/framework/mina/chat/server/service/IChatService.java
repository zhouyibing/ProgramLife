package framework.mina.chat.server.service;
/**
 * Created by Zhou Yibing on 2016/2/23.
 * �
 */
public interface IChatService {

    void sendRoomMessage(String sendUserId, String roomId, String content);

    void send2MyFriend(String sendUserId, String friendId, String content);

    void close();
}
