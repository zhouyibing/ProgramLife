package zhou.algorithm;

import java.util.Hashtable;

/**
 * Created by Zhou Yibing on 2016/3/9.
 * 基于hashtable和双向链表实现lru
 */
public class LRUCache<K,V> {
    private Hashtable<K,Entry> table;
    private Entry<K,V> first;
    private Entry<K,V> last;
    private int currentSize;
    private int size;

    public LRUCache(int size) {
        this.size = size;
        table = new Hashtable<>(size);
        currentSize=0;
    }

    public void put(K key,V value){
        Entry<K,V> entry = table.get(key);
        if(null==entry){
           if(currentSize>=size){
               removeLast();
           }else{
               currentSize++;
           }
            entry = new Entry<>(key,value);
        }
        //1.加入的节点加入链表头
        moveToHead(entry);
        //2.加入table中
        table.put(key,entry);
    }

    private void moveToHead(Entry entry) {
        //考虑几种情况 1.如果是头结点；2.如果是尾节点；
        //1.剔除节点；2.将节点设为头结点，原头节点后移
        //最后考虑只剩下一个节点，将last设为first;
        if(entry==first) return;
        if(entry.prev!=null)
            entry.prev.next=entry.next;
        if(entry.next!=null)
            entry.next.prev=entry.prev;
        if(last==entry)
            last = entry.prev;
        if(first!=null) {
            entry.next = first;
            first.prev=entry;
        }
        first = entry;
        entry.prev = null;
        if(last==null)
            last=first;
    }

    private void removeLast() {
        if(last!=null){
            if(last.prev!=null){
                last.prev.next=null;
            }else {
                //只有一个元素,移除后first和last都为空
                first = null;
            }
            last = last.prev;
        }
    }

    class Entry<K,V>{
        Entry<K,V> prev;
        Entry<K,V> next;
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
