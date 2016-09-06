package framework.compool;

import lombok.Data;

import java.util.UUID;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public class Connection {

    private String id;
    private String connectionName;
    private boolean connected;

    public Connection() {
        id = UUID.randomUUID().toString();
        connectionName = Thread.currentThread().getName()+"-"+System.currentTimeMillis();
    }

    public void connect(){
       connected=true;
    }

    public void close(){
       connected=false;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "id='" + id + '\'' +
                ", connectionName='" + connectionName + '\'' +
                ", connected=" + connected +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
