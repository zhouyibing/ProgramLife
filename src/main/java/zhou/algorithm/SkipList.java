package zhou.algorithm;

import java.util.Random;

public class SkipList{

    Entry head;//First element of the top level
    Entry tail;//Last element of the top level
    int n;//number of elements in the skip list
    int h;//height
    Random r;//coin toss

    public SkipList() {
        Entry p1=new Entry(Entry.negInf,null);
        Entry p2=new Entry(Entry.posInf,null);
        p1.right=p2;
        p2.left=p1;
        head=p1;
        tail=p2;
        n=0;
        h=0;
        r=new Random();
    }

    public Integer get(String k){
        Entry e = findEntry(k);
        if(e.key.equals(k))
            return e.value;
        return null;
    }

    public Integer put(String key, Integer value) {
        Entry p, q;
        int i = 0; // 查找适合插入的位子
        p = findEntry(key);
        // 如果跳跃表中存在含有key值的节点，则进行value的修改操作即可完成
        if(p.key.equals(key)) {
            Integer oldValue = p.value;
            p.value = value;
            return oldValue;
        }
        // 如果跳跃表中不存在含有key值的节点，则进行新增操作
        q = new Entry(key, value);
        q.left = p;
        q.right = p.right;
        p.right.left = q;
        p.right = q;
        // 再使用随机数决定是否要向更高level攀升
        while(r.nextDouble() < 0.5) {
            // 如果新元素的级别已经达到跳跃表的最大高度，则新建空白层
            if(i >= h) {
                addEmptyLevel();
            }
            // 从p向左扫描含有高层节点的节点
            while(p.up == null) {
                p = p.left;
            }
            p = p.up;
            // 新增和q指针指向的节点含有相同key值的节点对象
            // 这里需要注意的是除底层节点之外的节点对象是不需要value值的
            Entry z = new Entry(key, null);
            z.left = p;
            z.right = p.right;
            p.right.left = z;
            p.right = z;
            z.down = q;
            q.up = z;
            q = z;
            i = i + 1;
        }
        n = n + 1;
        // 返回null，没有旧节点的value值
        return null;
    }
    private void addEmptyLevel() {
        Entry p1, p2;
        p1 = new Entry(Entry.negInf, null);
        p2 = new Entry(Entry.posInf, null);
        p1.right = p2;
        p1.down = head;
        p2.left = p1;
        p2.down = tail;
        head.up = p1;
        tail.up = p2;
        head = p1;
        tail = p2;
        h = h + 1;
    }

    public Integer remove(String k){
        Entry e= findEntry(k);
        if(!e.key.equals(k))
            return null;
        Integer o = e.value;
        Entry i = null;
        while (e!=null){
            i=e.up;
            e.left.right=e.right;
            e.right.left=e.left;
            e=i;
        }
        return o;
    }

    private Entry findEntry(String key){
        Entry e=head;
        while (true){
            //从左向右查找，知道右节点的key值大于要查找的key值
            while (e.right.key!=Entry.posInf&&e.right.key.compareTo(key)<=0){
                e=e.right;
            }
            //如果有更低层的节点，则向低层移动
            if(e.down!=null)
                e=e.down;
            else
                break;
        }
        return e;//这里返回的e是key小于等于传入key的值。
    }

    static class Entry{
        String key;
        Integer value;
        Entry left;
        Entry right;
        Entry up;
        Entry down;
        // special
        public static final String negInf = "-oo";
        public static final String posInf = "+oo";

        public Entry(String key,Integer value) {
            this.key=key;
            this.value=value;
        }
    }

    public static void main(String[] args){
        SkipList skipList = new SkipList();
        skipList.put("12",12);
        skipList.put("17",17);
        skipList.put("20",20);
        skipList.put("25",25);
        skipList.put("31",31);
        skipList.put("38",38);
        skipList.put("39",39);
        skipList.put("44",44);
        skipList.put("50",50);
        skipList.put("55",55);
        System.out.println(skipList.get("39"));
        System.out.println(skipList.get("34"));
        System.out.println(skipList.remove("39"));
        System.out.println(skipList.get("39"));
    }
}
