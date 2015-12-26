package zhou.base.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestDeleteDuplicateElement {

	public static void main(String[] args) {
		List<Long> list = new ArrayList<Long>();
		for(int i = 0;i<1000000;i++){
			list.add(System.currentTimeMillis());
		}
		List<Long> list2 = new ArrayList<Long>();
		for(int i = 0;i<1000000;i++){
			list2.add(System.currentTimeMillis());
		}
		long begin = System.currentTimeMillis();
		removeDuplicate(list);
		System.out.println("consume times1:"+(System.currentTimeMillis()-begin));
		begin = System.currentTimeMillis();
		removeDuplicate2(list2);
		System.out.println("consume times2:"+(System.currentTimeMillis()-begin));
	}
	public static void removeDuplicate(List list) {  
		   for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {  
		     for ( int j = list.size() - 1 ; j > i; j -- ) {  
		       if (list.get(j).equals(list.get(i))) {  
		         list.remove(j);  
		       }   
		      }   
		    }   
		    System.out.println(list);  
		} 
	
	public static void removeDuplicate2(List list) {
	      HashSet h = new HashSet(list);  
	      list.clear();  
	      list.addAll(h);  
	      System.out.println(list);  
	}  
	
	public static void removeDuplicateWithOrder(List list) {  
	     Set set = new HashSet();  
	      List newList = new ArrayList();  
	   for (Iterator iter = list.iterator(); iter.hasNext();) {  
	          Object element = iter.next();  
	          if (set.add(element))  
	             newList.add(element);  
	       }   
	      list.clear();  
	      list.addAll(newList);  
	     System.out.println( " remove duplicate " + list);  
	}  
}
