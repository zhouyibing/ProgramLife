package zhou.program.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
public class NameStatistic {

	
	public static void main(String[] args) throws IOException {

		BufferedReader buffReader = new BufferedReader(new FileReader(new File("src\\zhou\\program\\file\\name.txt")));
		/*SortedMap<String,Integer> nameMap=new TreeMap<String,Integer>();
		
		while(null!=(line=buffReader.readLine())){
			String[] info=line.split(",");
			if(null!=nameMap.get(info[1]))
				nameMap.put(info[1],nameMap.get(info[1])+1);
			else
				nameMap.put(info[1],1);
		}
		Set<String> keySet=nameMap.keySet();
		Iterator<String> it=keySet.iterator();
		
		String key;
		while(it.hasNext()){
			key=it.next();
			System.out.println(key+","+nameMap.get(key));
		}*/
		Map<String,Integer> nameMap = new HashMap<String,Integer>();
		dealFile(buffReader,nameMap);
		sortResult(nameMap);
		buffReader.close();
	}
	
	private static void dealFile(BufferedReader buffReader,Map<String,Integer> nameMap) throws IOException{
		String line="";
		while(null!=(line=buffReader.readLine())){
			String[] info=line.split(",");
			if(nameMap.containsKey(info[1]))
				nameMap.put(info[1],nameMap.get(info[1])+1);
			else
				nameMap.put(info[1],1);
		}
	}
	
	private static void sortResult(Map<String,Integer> nameMap){
		SortedSet<Name> treeSet=new TreeSet<Name>(new Comparator<Name>() {

			@Override
			public int compare(Name o1, Name o2) {
				Name name1 = o1;
				Name name2 = o2;
				if(name1.count>name2.count)
					return 1;
				else if(name1.count<name2.count)
					return -1;
				else
					return name1.name.compareTo(name2.name);
			}
			
		});
		
		Set<String> keySet = nameMap.keySet();
	    for(Iterator<String> it=keySet.iterator();it.hasNext();){
	    	String key=it.next();
	    	Name name=new Name(key);
	    	name.setCount(nameMap.get(key));
	    	treeSet.add(name);
	    }
		
        Iterator<Name> it=treeSet.iterator();
        Name key;
		while(it.hasNext()){
			key=it.next();
			System.out.println(key.name+","+key.count);
		}
	}
	
	static class Name{
		String name;
		int count;
	
		public Name(String name){
			this.name=name;
		}
		
		public void setCount(int count){
			this.count=count;
		}
	}
}
