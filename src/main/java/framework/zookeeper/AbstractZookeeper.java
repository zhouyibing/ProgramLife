package framework.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Zhou Yibing on 2015/10/20.
 */
public class AbstractZookeeper implements Watcher{
    private static Log log = LogFactory.getLog(ZookeeperOperator.class.getName());

    private static final int SESSION_TIME = 2000;
    protected ZooKeeper zookeeper;
    protected CountDownLatch countDownLatch=new CountDownLatch(1);

    public void connect(String host) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(host,SESSION_TIME,this);
        countDownLatch.await();
        log.info("connected to the zookeeper server ["+host+"] success!");
    }

    @Override
    public void process(WatchedEvent event) {
       log.info(event.toString());
        if(event.getState()== Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    public void close() throws InterruptedException {
        zookeeper.close();
    }
}
