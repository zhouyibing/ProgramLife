package framework.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.KeeperException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Zhou Yibing on 2015/11/4.
 */
public class NodeCacheTest {

    private static final String PATH="/curator/nodeCache";
    public static void main(String[] args){
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183", new ExponentialBackoffRetry(1000, 3));
        client.start();
        NodeCache nodeCache = new NodeCache(client,PATH);
        try {
            nodeCache.start();
            prcocessCommand(client, nodeCache);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(nodeCache);
        }
    }

    private static void addListener(final NodeCache cache){
        //getCurrentData()将得到节点当前的状态，通过它的状态可以得到当前的值。 可以使用public void addListener(NodeCacheListener listener)监控节点状态的改变。
        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                if(cache.getCurrentData()!=null){
                    System.out.println("Node["+cache.getCurrentData().getPath()+"] changed,value is["+new String(cache.getCurrentData().getData())+"]");
                }
            }
        };
        //attach this listener to the cache
        cache.getListenable().addListener(listener);
    }

    private static void prcocessCommand(CuratorFramework client, NodeCache nodeCache) {

        printHelp();
        addListener(nodeCache);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean isQuit =false;
        try {
            while(!isQuit){
                System.out.print("> ");
                String line = null;
                line = reader.readLine();
                if(line==null) break;
                String command = line.trim();
                String[] parts = command.split("\\s");
                if(parts.length==0) continue;
                String operation = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                if(operation.equalsIgnoreCase("help")||operation.equalsIgnoreCase("?")){
                    printHelp();
                }else if(operation.equalsIgnoreCase("q")||operation.equalsIgnoreCase("quit")){
                    isQuit=true;
                }else if(operation.equalsIgnoreCase("show")){
                    show(nodeCache);
                }else if(operation.equalsIgnoreCase("remove")){
                    remove(client);
                }else if(operation.equalsIgnoreCase("set")){
                    setValue(client,operation,args);
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Bye-Bye!");
    }

    private static void show(NodeCache cache){
        if(cache.getCurrentData()!=null){
            System.out.println("The node["+cache.getCurrentData().getPath()+"] value is:"+new String(cache.getCurrentData().getData()));
        }else {
            System.out.println("The node don't not set a value");
        }
    }

    private static void remove(CuratorFramework client){
        try {
            client.delete().forPath(PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setValue(CuratorFramework client,String command,String[] args) throws Exception {
        if(args.length!=1){
            System.err.println("syntax error (expected set <value>): " + command);
            return;
        }
        byte[] bytes = args[0].getBytes();
        try {
            client.setData().forPath(PATH,bytes);
        } catch (KeeperException.NoNodeException e) {
            client.create().creatingParentsIfNeeded().forPath(PATH,bytes);
        }
    }

    private static void printHelp() {
        System.out.println("This is an example of NodeCache in curator.You can use the following command:\n");
        System.out.println("set <value>: Adds or updates a node with the given name");
        System.out.println("remove: Deletes the node with the given name");
        System.out.println("show: Display the node's value in the cache");
        System.out.println("quit: Quit the example");
        System.out.println();
    }
}
