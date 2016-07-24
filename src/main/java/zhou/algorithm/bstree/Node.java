package zhou.algorithm.bstree;

import lombok.Data;

@Data
public class Node<K extends Comparable>{
    public K value;
    public Node parent;
    public Node left;
    public Node right;

          public Node(K value) {
               this.value = value;
          }
     }