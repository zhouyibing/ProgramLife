package zhou.algorithm;

import java.io.Serializable;

public class SinglyLinkedList<T>{
     Node head;
     Node tail;
     int size;
    public void add(T v){
        if(null==head) {
            head = new Node(v, null);
            tail=head;
            size++;
            return;
        }
        Node n = new Node(v,null);
        tail.next=n;
        tail=n;
        size++;
    }
    public Node<T> head(){
        return head;
    }
    public T getHeadValue(){
        if(null==head||head.value==null) return null;
        return (T) head.value;
    }

    public int size(){
        return size;
    }

    @Override
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

    public void reverse(){
        if(null==head) return;
        Node n = head;
        Stack stack = new Stack();
        while (n!=null){
            stack.push(n.value);
            n=n.next;
        }
        SinglyLinkedList list = new SinglyLinkedList();
        while (stack.size()!=0) {
            list.add(stack.pop());
        }
        head = list.head;
        tail = list.tail;
        size = list.size;
    }
    /**
     *倒序打印链表
     * 我们可以用栈实现这种顺序，从头到尾遍历链表，每遍历一个节点将它保存到栈中，直到整个链表遍历完成，再从栈中依次取出节点并打印对应的值。
     */
    public String reversePrint(){
        if(null==head) return "[]";
        Node n = head;
        Stack stack = new Stack();
        while (n!=null){
            stack.push(n.value);
            n=n.next;
        }
        return stack.toString();
    }

    public void clear(){
        head=null;
        tail=null;
        size=0;
    }

    class Node<T> implements Serializable{
        T value;
        Node next;

        public Node() {
        }

        public Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args){
        SinglyLinkedList<String> list = new SinglyLinkedList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        System.out.println(list);
        System.out.println(list.reversePrint());
        list.reverse();
        System.out.println(list);
    }
}
