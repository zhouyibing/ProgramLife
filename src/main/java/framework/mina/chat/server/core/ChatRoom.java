package framework.mina.chat.server.core;

import framework.mina.chat.server.model.Message;
import framework.mina.chat.server.model.Room;
import framework.mina.chat.server.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
public class ChatRoom extends Chat{

    private ConcurrentHashMap<String,Room> roomMap = new ConcurrentHashMap<>();
    private static ChatRoom chatRoom;

    private ChatRoom() {
    }

    public static ChatRoom getChatRoom(){
        if(null==chatRoom){
            synchronized (ChatRoom.class){
                if(null==chatRoom)
                 chatRoom=new ChatRoom();
                 chatRoom.start();
            }
        }
        return chatRoom;
    }

    public void sendMessage(String sendClient,String recivedClient,String content){
        Room room = roomMap.get(recivedClient);
        List<User> users  = room.getRoomMembers();
        List<Message> messages = new ArrayList<Message>(users.size()-1);

        for(User user:users){
            if(user.getUserId().equals(sendClient)) continue;
            Message message = new Message();
            message.setMessageId(UUID.randomUUID().toString());
            message.setMessageType(Message.MessageType.GROUP);
            message.setRelationId(recivedClient);
            message.setSendUser(sendClient);
            message.setReceivedUser(user.getUserId());
            messages.add(message);
        }
        super.putMessage((Message[]) messages.toArray());
    }

    void addRoom(Room room){
         room.setRoomId(UUID.randomUUID().toString());
         roomMap.put(room.getRoomId(),room);
    }

    void removeRoom(String roomId){
        roomMap.remove(roomId);
    }
}
