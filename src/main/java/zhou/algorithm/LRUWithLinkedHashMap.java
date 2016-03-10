package zhou.algorithm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Zhou Yibing on 2016/3/10.
 * 基于linkedhashmap实现lru算法
 * 1.该算法实现的利用linkdedhashmap在put元素会根据重载的removeEldestEntry判断是否需要移除最久的元素
 * 2.及在get元素是，会将命中的元素移到链表头部。recordAccess方法移动了元素
 */
public class LRUWithLinkedHashMap <K,V>{

    private LinkedHashMap<K,V>table = null;
    static final float LOAD_FACTOR = 0.75f;
    private int cacheSize;

    public LRUWithLinkedHashMap(final int cacheSize) {
        this.cacheSize = cacheSize;
        int tableLength = (int)Math.ceil(cacheSize/LOAD_FACTOR)+1;
        table = new LinkedHashMap<K,V>(tableLength,LOAD_FACTOR,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size()>LRUWithLinkedHashMap.this.cacheSize;
            }
        };
    }

    public synchronized V  put(K key,V value){
        return table.put(key,value);
    }

    public V get(K key){
        return table.get(key);
    }

    public synchronized V remove(K key){
        return table.remove(key);
    }

    public Set<Entry<K,V>> entrySet(){
        return table.entrySet();
    }

    public void clear(){
        table.clear();
    }

    public static void main(String[] args){
        LRUWithLinkedHashMap<String,Integer> lruWithLinkedHashMap = new LRUWithLinkedHashMap(3);
        lruWithLinkedHashMap.put("a",1);
        lruWithLinkedHashMap.put("b",2);
        lruWithLinkedHashMap.put("c",3);
        System.out.println(lruWithLinkedHashMap.get("a"));
        lruWithLinkedHashMap.put("d",4);
        System.out.println(lruWithLinkedHashMap.get("a"));
        Set<Entry<String,Integer>> set = lruWithLinkedHashMap.entrySet();
        for(Iterator it=set.iterator();it.hasNext();){
            Entry<String,Integer> entry = (Entry<String, Integer>) it.next();
            System.out.print(entry.getKey() + "," + entry.getValue()+" ");
        }
    }
}
