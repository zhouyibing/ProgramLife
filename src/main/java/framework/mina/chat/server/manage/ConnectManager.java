package framework.mina.chat.server.manage;

import framework.mina.chat.server.model.Room;
import framework.mina.chat.server.model.User;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Zhou Yibing on 2016/2/24.
 */
public class ConnectManager implements IConnectManager{

    private ConcurrentHashMap<String,Connection> connections = new ConcurrentHashMap<>();
    private static ConnectManager connectManager;

    private ConnectManager() {
    }

    public static  ConnectManager getConnectManager(){
        if(connectManager==null){
            synchronized (ConnectManager.class){
                if(connectManager==null)
                    connectManager = new ConnectManager();
            }
        }
        return connectManager;
    }

    @Override
    public Connection getConnection(String userId) {
        return connections.get(userId);
    }

    @Override
    public List<Connection> getConnections(Room room) {
        if(null==room) return null;
        List<User> users =room.getRoomMembers();
        List<Connection> list = new ArrayList<>();
        for(User user:users){
            Connection connection = getConnection(user.getUserId());
            if(null!=connection)
            list.add(connection);
        }
        return list;
    }

    @Override
    public void disconnect(String userId){
        Connection connection = getConnection(userId);
        if(null==connection) return;
        connection.disconnect();
        connection = null;
        connections.remove(userId);
    }

    @Override
    public Connection createConnection(User user, IoSession ioSession) {
        if(connections.containsKey(user.getUserId())) return getConnection(user.getUserId());

        user.setOnline(true);
        InetSocketAddress addr = (InetSocketAddress) ioSession.getRemoteAddress();
        user.setLoginIp(addr.getAddress().getHostAddress());
        user.setLoginTime(new Date(ioSession.getCreationTime()));
        Connection connection = new Connection(user,ioSession);
        connections.put(user.getUserId(),connection);
        return connection;
    }
}
