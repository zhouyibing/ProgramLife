package zhou.algorithm;

import java.io.Serializable;

public class Stack<T>{
    private Node<T> top;
    private int size;
    public void push(T v){
        if(null==top){
            top=new Node<>(v,null);
            size++;
            return;
        }
        Node n = new Node(v,top);
        top=n;
        size++;
    }
    public T pop(){
        if(null==top) return null;
        T v = top.value;
        top = top.next;
        size--;
        return v;
    }

    public Node top(){
        return top;
    }

    public int size(){
      return size;
    }
    public void clear(){
        size=0;
        top=null;
    }

    class Node<T> implements Serializable {
        T value;
        Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        if(null==top) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node n = top;
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
        Stack<String> stack = new Stack<>();
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");
        System.out.println(stack);
        while (stack.size()!=0)
            System.out.println(stack.pop());
    }
}
