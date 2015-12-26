package framework.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Zhou Yibing on 2015/11/4.
 */
public class TreeNodeCacheTest {
    private static final String PATH = "/curator/treeCache";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        TreeCache cache = null;
        try {
            client = CuratorFrameworkFactory.newClient("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183", new ExponentialBackoffRetry(1000, 3));
            client.start();

            cache = new TreeCache(client, PATH);
            cache.start();
            processCommands(client, cache);
        } finally {
            CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static void processCommands(CuratorFramework client, TreeCache cache) {

        printHelp();
        addListener(cache);
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
                }else if(operation.equalsIgnoreCase("list")){
                    list(cache);
                }else if(operation.equalsIgnoreCase("remove")){
                    remove(client,operation,args);
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

    private static void setValue(CuratorFramework client, String operation, String[] args) throws Exception {
        if(args.length!=2){
            System.err.println("syntax error (expected remove <path>): " + operation);
            return;
        }
        String name=args[0];
        if(name.contains("/")){
            System.err.println("Invalid node name" + name);
            return;
        }
        String path = ZKPaths.makePath(PATH,name);
        try {
            client.setData().forPath(path,args[1].getBytes());
        } catch (KeeperException.NoNodeException e) {
            client.create().creatingParentsIfNeeded().forPath(path,args[1].getBytes());
        }
    }

    private static void remove(CuratorFramework client,String command,String[] args) {

        if(args.length!=1){
            System.err.println("syntax error (expected remove <path>): " + command);
            return;
        }
        String name=args[0];
        if(name.contains("/")){
            System.err.println("Invalid node name" + name);
            return;
        }
        String path = ZKPaths.makePath(PATH,name);
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void list(TreeCache treeCache) {

        if(treeCache.getCurrentChildren(PATH).size()==0){
            System.out.println("The path["+PATH+"] has no child node");
        }else{
            for(Map.Entry<String,ChildData> entry:treeCache.getCurrentChildren(PATH).entrySet()){
                System.out.println(entry.getKey()+"="+entry.getValue());
            }
        }
    }

    private static void addListener(final TreeCache cache){
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
              switch (treeCacheEvent.getType()){
                  case NODE_ADDED:
                      System.out.println("TreeNode added:"+ ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath())+",value:"+new String(treeCacheEvent.getData().getData()));
                      break;
                  case NODE_UPDATED:
                      System.out.println("TreeNode changed:"+ ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath())+",value:"+new String(treeCacheEvent.getData().getData()));
                      break;
                  case NODE_REMOVED:
                      System.out.println("TreeNode removed:"+ ZKPaths.getNodeFromPath(treeCacheEvent.getData().getPath()));
                      break;
                  default:
                      System.out.println("Other event:"+treeCacheEvent.getType().name());
              }
            }
        };
        cache.getListenable().addListener(listener);
    }

    private static void printHelp() {
        System.out.println("An example of using PathChildrenCache. This example is driven by entering commands at the prompt:\n");
        System.out.println("set <name> <value>: Adds or updates a node with the given name");
        System.out.println("remove <name>: Deletes the node with the given name");
        System.out.println("list: List the nodes/values in the cache");
        System.out.println("quit: Quit the example");
        System.out.println();
    }
}
