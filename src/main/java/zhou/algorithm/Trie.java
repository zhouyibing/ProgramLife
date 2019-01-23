package zhou.algorithm;

import java.util.LinkedList;

public class Trie {
    class Node{
        char content;//存储的字符
        boolean isEnd;//是否字符串结束位置
        int count;//共享这个字符的字符串个数
        LinkedList<Node> childList;//子节点列表

        public Node(char content) {
            this.content = content;
            isEnd=false;
            childList=new LinkedList<>();
            count=0;
        }
        public Node subNode(char c){
            if(childList!=null){
                for(Node eachChild:childList){
                    if(eachChild.content==c)
                        return eachChild;
                }
            }
            return null;
        }
    }
    private Node root;
    public Trie(){
        root = new Node('\0');
    }

    public void insert(String word){
        if(search(word)) return;
        Node current = root;
        for(int i=0;i<word.length();i++){
            Node child =current.subNode(word.charAt(i));
            if(child==null){
                Node n = new Node(word.charAt(i));
                current.childList.add(n);
                current=n;
            }else{
                current=child;
            }
            current.count++;
        }
        current.isEnd=true;
    }

    public boolean search(String word){
        Node current = root;
        for (int i=0;i<word.length();i++){
            if(current.subNode(word.charAt(i))==null)
                return false;
            else
                current=current.subNode(word.charAt(i));
        }
        return current.isEnd;
    }

    public void deleteWord(String word){
        if(!search(word)) return;
        Node current = root;
        for(char c:word.toCharArray()){
            Node child = current.subNode(c);
            if (child.count==1){
                current.childList.remove(child);
                return;
            }else{
                child.count--;
                current=child;
            }
        }
        current.isEnd=false;
    }

    public static void main(String[] args){
        Trie trie = new Trie();
        trie.insert("ball");
        trie.insert("bals");
        trie.insert("sense");
        System.out.println(trie.search("balls"));
        System.out.println(trie.search("ba"));
        trie.deleteWord("balls");
        System.out.println(trie.search("balls"));
        System.out.println(trie.search("ball"));
    }
}
