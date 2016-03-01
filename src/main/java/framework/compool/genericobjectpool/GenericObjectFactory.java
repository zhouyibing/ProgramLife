package framework.compool.genericobjectpool;

import framework.compool.Connection;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public class GenericObjectFactory implements PoolableObjectFactory {
    @Override
    public Object makeObject() throws Exception {
        Connection connection = new Connection();
        System.out.println("create connection:"+connection);
        return connection;
    }

    @Override
    public void destroyObject(Object obj) throws Exception {
        Connection con = (Connection)obj;
        con.close();
        System.out.println("destory connection:"+con);
    }

    @Override
    public boolean validateObject(Object obj) {
        Connection con = (Connection)obj;
        if(con.getId()==null||con.getConnectionName()==null) return false;
        System.out.println("validate connection:"+con);
        return true;
    }

    @Override
    public void activateObject(Object obj) throws Exception {
        Connection con = (Connection)obj;
        con.connect();
        System.out.println("active connection:"+con);
    }

    @Override
    public void passivateObject(Object obj) throws Exception {

    }
}
