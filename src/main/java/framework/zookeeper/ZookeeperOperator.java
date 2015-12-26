package framework.zookeeper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zhou Yibing on 2015/10/21.
 */
public class ZookeeperOperator extends  AbstractZookeeper{

    private static Log log = LogFactory.getLog(ZookeeperOperator.class.getName());

    /**
     * 此处采用的是CreateMode是PERSISTENT  表示The znode will not be automatically deleted upon client's disconnect.
     * EPHEMERAL 表示The znode will be deleted upon the client's disconnect.
     */
    public void create(String path,byte[] data) throws KeeperException, InterruptedException {
        this.zookeeper.create(path,data,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public boolean isNodeExists(String path) throws KeeperException, InterruptedException {
        Stat stat = this.zookeeper.exists(path,false);
        log.info("the path["+path+"] status is:"+stat);
        return null!=stat;
    }

    public void createOrReplace(String path,byte[] data) throws KeeperException, InterruptedException {
        if(this.isNodeExists(path))
            this.delete(path,-1);
        else
            this.zookeeper.create(path,data,Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
    }

    public void  delete(String path,int version) throws KeeperException, InterruptedException {
        this.zookeeper.delete(path,version);
    }
    public void createEphemeralSeq(String path,byte[] data) throws KeeperException, InterruptedException {
        this.zookeeper.create(path,data,Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void getChild(String path) throws KeeperException, InterruptedException {
        List<String> list = this.zookeeper.getChildren(path,false);
        if(CollectionUtils.isEmpty(list)){
            log.info("the path[" + path + "] has no node");
        }else{
            for(String child:list){
                log.info("node: " + child);
            }
        }
    }

    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return this.zookeeper.getData(path,false,null);
    }

    public byte[] getDataAndWatched(String path) throws KeeperException, InterruptedException {
        return this.zookeeper.getData(path,true,null);
    }

    public void setData(String path,byte[] data,int ver) throws KeeperException, InterruptedException {
        this.zookeeper.setData(path,data,ver);
    }

    public static void main(String[] args){
        ZookeeperOperator zookeeperOperator = new ZookeeperOperator();
        try {
            zookeeperOperator.connect("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183");
            byte[] data = new byte[]{'a','b','c','d'};
            if(!zookeeperOperator.isNodeExists("/iwjw")){
                zookeeperOperator.create("/iwjw",null);
            }
            log.info("get data from path[/iwjw]:"+zookeeperOperator.getData("/iwjw"));
            if(!zookeeperOperator.isNodeExists("/iwjw")) {
                zookeeperOperator.create("/iwjw/zk", "test".getBytes());
            }
            log.info("get data from[/iwjw/zk]:" + new String(zookeeperOperator.getData("/iwjw/zk")));

            zookeeperOperator.createOrReplace("/iwjw/zoo", "zoo".getBytes());
            log.info("get data from[/iwjw/zoo]:" + new String(zookeeperOperator.getData("/iwjw/zoo")));

            zookeeperOperator.createEphemeralSeq("/iwjw/zoo", "zoo".getBytes());
            log.info("get data from[/iwjw/zoo]:" + new String(zookeeperOperator.getData("/iwjw/zoo")));

            log.info("get data from[/iwjw/zk]:" + new String(zookeeperOperator.getDataAndWatched("/iwjw/zk")));
            zookeeperOperator.setData("/iwjw/zk","zk".getBytes(),-1);
           // zookeeperOperator.setData("/iwjw/zk","zk".getBytes(),-1);
            zookeeperOperator.getChild("/");

            //Thread.sleep(100000);
            zookeeperOperator.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
