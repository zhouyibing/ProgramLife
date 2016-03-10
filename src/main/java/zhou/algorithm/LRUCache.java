package zhou.algorithm;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Zhou Yibing on 2016/3/9.
 * 基于hashtable和双向链表实现lru 支持并发
 */
public class LRUCache<K,V> {
    private Hashtable<K,Entry> table;
    private Entry<K,V> first;
    private Entry<K,V> last;
    private Lock lock = new ReentrantLock();
    private volatile int currentSize;
    private int size;

    public LRUCache(int size) {
        this.size = size;
        table = new Hashtable<>(size);
        currentSize=0;
    }

    public void put(K key,V value){
        Entry<K,V> entry = table.get(key);
        try {
            lock.lock();
            if (null == entry) {
                if (currentSize >= size) {
                    removeLast();
                } else {
                    currentSize++;
                }
                entry = new Entry<>(key, value);
            }
            entry.value = value;
            //1.加入的节点加入链表头
            moveToHead(entry);
        }finally {
            lock.unlock();
        }
        //2.加入table中
        table.put(key,entry);
    }

    public V get(K key){
        Entry<K,V> entry = table.get(key);
        V value = null;
        try{
            lock.lock();
            if(null!=entry){
                moveToHead(entry);
                value = entry.value;
            }
        }finally {
            lock.unlock();
        }
        return value;
    }

    public  Set<Entry<K,V>> entrySet(){
        Set<Entry<K,V>> set = new LinkedHashSet<>();
        Entry entry = first;
        while (entry!=null){
            set.add(entry);
            entry=entry.next;
        }
        return set;
    }

    public V remove(K key){
        Entry<K,V> entry = table.get(key);
        V value = null;
        try{
            lock.lock();
            if(entry!=null){
                value = entry.value;
                if(entry.prev!=null)
                entry.prev.next=entry.next;
                if(entry.next!=null)
                entry.next.prev=entry.prev;
                if(entry==first)
                    first = entry.next;
                if(entry==last)
                    last=entry.prev;
            }
        }finally {
            lock.unlock();
        }
        return value;
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

    public static void main(String[] args) throws InterruptedException {
       final LRUCache<String,String> lruCache = new LRUCache<>(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<100000;i++) {
            final int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    lruCache.put(Thread.currentThread().getName()+"-"+ finalI, finalI+"");
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        Set<Entry<String,String>> set = lruCache.entrySet();
        for(Iterator it=set.iterator();it.hasNext();){
            Entry<String,String> next = (Entry<String, String>) it.next();
            System.out.print(next.key + "," + next.value + " ");
        }
    }

    static class Entry<K,V>{
        volatile  Entry<K,V> prev;
        volatile  Entry<K,V> next;
        final K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
