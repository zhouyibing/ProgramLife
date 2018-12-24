package zhou.algorithm;

public class SortedLinkedList<T extends Comparable>{
    static class Node<T extends Comparable> {
        private T value;
        private Node next;
        private Node pre;

        public Node(T value,Node pre, Node next) {
            this.value = value;
            this.next = next;
            this.pre=pre;
        }
    }
    public SortedLinkedList() {
    }
    public SortedLinkedList(boolean desc) {
        this.desc=true;
    }

    private Node head;
    private boolean desc;
    private int size;
    public void add(T v) {
        if(null==head){
            head=new Node(v,null,null);
            size++;
            return;
        }
        Node n = new Node(v,null,null);
        Node h = head;
        int cmp =h.value.compareTo(v);
        if((!desc&&cmp>=0)||(desc&&cmp<0)){
            n.next=h;
            n.pre=h.pre;
            h.pre=n;
            head=n;
            size++;
            return;
        }else {
            if(null==h.next){
                h.next=n;
                n.pre=h;
                size++;
                return;
            }
            h=h.next;
        }

        while (h!=null){
            cmp = h.value.compareTo(v);
            if((!desc&&cmp>=0)||(desc&&cmp<0)){
                n.next=h;
                n.pre=h.pre;
                if(h.pre==null) head=n;
                else h.pre.next=n;
                h.pre=n;
                size++;
                return;
            }else{
                if(null==h.next){
                    h.next=n;
                    n.pre=h;
                    size++;
                    break;
                }
                h=h.next;
            }
        }
    }

    private int size(){
        return size;
    }
    public Node merge(Node l1,Node l2){
        if(null==l1)
            return l2;
        if(l2==null)
            return l1;
        Node nHead = null;
        int cmp = l1.value.compareTo(l2.value);
        if(cmp<0){
            nHead=l1;
            nHead.next=merge(l1.next,l2);
        }else{
            nHead=l2;
            nHead.next=merge(l1,l2.next);
        }
        return nHead;
    }
    public String toString() {
        if(null==head) return "[]";
        StringBuilder sb = new StringBuilder();
        Node n = head;
        sb.append("[");
        while (n!=null){
            sb.append(n.value);
            n=n.next;
            if(null!=n)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
    public static void main(String[] args){
        SortedLinkedList<Integer> linkedList = new SortedLinkedList();
        linkedList.add(123);
        linkedList.add(23);
        linkedList.add(14);
        linkedList.add(244);
        linkedList.add(244);
        linkedList.add(125);
        linkedList.add(43);
        linkedList.add(1);
        linkedList.add(21);
        linkedList.add(53);
        System.out.println(linkedList);

        SortedLinkedList<Integer> linkedList2 = new SortedLinkedList();
        linkedList2.add(1);
        linkedList2.add(16);
        linkedList2.add(35);
        linkedList2.add(58);
        linkedList2.add(156);
        linkedList2.add(256);
        linkedList2.add(20);
        linkedList2.add(25);
        System.out.println(linkedList2);
        SortedLinkedList<Integer> merge = new SortedLinkedList();
        merge.head=merge.merge(linkedList.head,linkedList2.head);
        System.out.println(merge);
    }
}
