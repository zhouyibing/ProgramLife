package framework.compool;

import lombok.Data;

import java.util.UUID;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
@Data
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
}
