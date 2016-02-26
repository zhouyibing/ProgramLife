package framework.mina.chat.server.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Zhou Yibing on 2016/2/23.
 * ������
 */
@Data
public class Room {
    private String roomId;
    private String roomName;
    private List<User> roomMembers;
}
