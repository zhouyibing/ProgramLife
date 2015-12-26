package zhou.algorithm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Zhou Yibing on 2015/12/21.
 * 一致性hash算法java实现
 */
public class ConsistentHash {

    private TreeMap<Long,Object> nodes;//服务器hash节点
    private static final int VIRTUALNODE_NUM=4;//虚拟节点个数
    private List<Object> serverNodes;//真实服务器信息

    public void init(){//initialization
        serverNodes = new ArrayList<Object>();
        serverNodes.add("192.168.80.1");
        serverNodes.add("192.168.80.2");
        serverNodes.add("192.168.80.3");
        serverNodes.add("192.168.80.4");
        nodes = new TreeMap<Long,Object>();
        for(int i=0;i<serverNodes.size();i++){
            for(int j=0;j<VIRTUALNODE_NUM;j++){
                nodes.put(hash(computeMD5("shard-"+i+"-node-"+j),j),serverNodes.get(i));
            }
        }
    }

    /**
     * 打印圆环节点数据
     */
    public void printMap() {
        System.out.println(nodes);
    }

    public Object getServer(String key){
        Random ran = new Random();
        Long keyHash = hash(computeMD5(key),ran.nextInt(VIRTUALNODE_NUM));
        SortedMap<Long,Object> sortedMap = nodes.tailMap(keyHash);
        if(sortedMap.isEmpty()){
            return nodes.firstEntry().getValue();
        }else{
            return sortedMap.get(sortedMap.firstKey());
        }
    }

    public Long hash(byte[] digest,int nTime){
        long rv = ((long) (digest[3+nTime*VIRTUALNODE_NUM] & 0xFF) << 24)
                | ((long) (digest[2+nTime*VIRTUALNODE_NUM] & 0xFF) << 16)
                | ((long) (digest[1+nTime*VIRTUALNODE_NUM] & 0xFF) << 8)
                | (digest[0+nTime*4] & 0xFF);

        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    public byte[] computeMD5(String k){

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.reset();
        byte[] bytes = null;
        try {
            bytes = k.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    public static void main(String[] args){
        ConsistentHash consistentHash = new ConsistentHash();
        consistentHash.init();
        consistentHash.printMap();
        for(int i=0;i<50;i++){
            System.out.println(consistentHash.getServer("test"+i));
        }
    }
}
