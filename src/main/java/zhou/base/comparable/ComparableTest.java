package zhou.base.comparable;

import java.util.Set;
import java.util.TreeSet;

public class ComparableTest {
	
	public static void main(String[] args) {

		Set<Parent> set=new TreeSet<Parent>();
		set.add(new Parent());
		set.add(new Child());
	}

	static class Parent implements Comparable{

		@Override
		public int compareTo(Object o) {
			System.out.println("invoke parent's compareTo method!");
			return 0;
		}
		
	}
	
	static class Child extends Parent{

		@Override
		public int compareTo(Object o) {
			System.out.println("invoke child's compareTo method!");
			return 0;
		}
	}
}
