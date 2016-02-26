package framework.mina.chat.server.manage;

import framework.mina.chat.server.model.Room;
import framework.mina.chat.server.model.User;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * Created by Zhou Yibing on 2016/2/23.
 */
public interface IConnectManager {

    Connection getConnection(String userId);

    List<Connection> getConnections(Room room);

    void disconnect(String userId);

    Connection createConnection(User user, IoSession ioSession);
}
