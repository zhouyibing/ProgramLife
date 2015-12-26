package zhou.base.collection;

import java.util.ArrayList;
import java.util.List;

public class ListTest3 {

	public static void main(String[] args) {
		Person p = new Person();
		List<Person> ps = new ArrayList<Person>();
		for(int i=0;i<10;i++){
			p.setId(i+"");
			p.setName("name-"+i);
			ps.add(p);
		}
		
		for(int i=0;i<10;i++){
		  System.out.println(ps.get(i).getId());
		  System.out.println(ps.get(i).getName());
		}
	}

	static class Person{
		String name;
		String id;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
}
