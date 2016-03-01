package framework.compool.keyedobjectpool;

import framework.compool.Connection;
import org.apache.commons.pool.KeyedPoolableObjectFactory;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public class KeyedObjectFactory implements KeyedPoolableObjectFactory {

    @Override
    public Object makeObject(Object key) throws Exception {
        Connection con = new Connection();
        con.setId((String) key);
        System.out.println("create connection:"+con);
        return con;
    }

    @Override
    public void destroyObject(Object key, Object obj) throws Exception {
        Connection con = (Connection)obj;
        con.close();
        System.out.println("destory connection:"+con);
    }

    @Override
    public boolean validateObject(Object key, Object obj) {
        Connection con = (Connection)obj;
        if(con.getId()==null||con.getConnectionName()==null) return false;
        System.out.println("validate connection:"+con);
        return true;
    }

    @Override
    public void activateObject(Object key, Object obj) throws Exception {
        Connection con = (Connection)obj;
        con.connect();
        System.out.println("active connection:"+con);
    }

    @Override
    public void passivateObject(Object key, Object obj) throws Exception {

    }
}
