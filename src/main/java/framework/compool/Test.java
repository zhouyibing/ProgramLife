package framework.compool;

import framework.compool.genericobjectpool.GenericObjectFactory;
import framework.compool.keyedobjectpool.KeyedObjectFactory;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPool;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhou Yibing on 2016/2/26.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        testGenericObjectPool();
    }

    public static void testGenericObjectPool() throws Exception {
        GenericObjectPool genericObjectPool = new GenericObjectPool(new GenericObjectFactory());
        genericObjectPool.setMaxIdle(2);
        genericObjectPool.setMinIdle(1);
        genericObjectPool.setMaxActive(10);
        genericObjectPool.setLifo(false);
        for(int i=0;i<13;i++){
            Connection con=null;
            try {
                con = (Connection) genericObjectPool.borrowObject();
            } catch (Exception e) {
                e.printStackTrace();
                genericObjectPool.invalidateObject(con);
            }finally {
                genericObjectPool.returnObject(con);
            }
        }
    }

    public static void testKeyedObjectPool(){
        final KeyedObjectPool keyedObjectPool = new StackKeyedObjectPool(new KeyedObjectFactory());
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        for(int i=0;i<10;i++){
            final String key ="conn_"+i;
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Connection con = null;
                    try {
                        con = (Connection) keyedObjectPool.borrowObject(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            keyedObjectPool.invalidateObject(key,con);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }finally {
                        try {
                            keyedObjectPool.returnObject(key,con);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            },1,1, TimeUnit.SECONDS);

        }
    }
}
