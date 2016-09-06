package framework.mina.chat.server.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ������
 */
public class Room {
    private String roomId;
    private String roomName;
    private List<User> roomMembers;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<User> getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(List<User> roomMembers) {
        this.roomMembers = roomMembers;
    }
}
