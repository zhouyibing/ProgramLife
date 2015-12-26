package framework.guava.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Joiner;
public class TestJoiner {

    @Test
    public void testJoiner(){
    	List<String> list = new ArrayList<String>();
		list.add(null);
		list.add("a");
		list.add(null);
		list.add("b");
		list.add("c");
		
		//joined element in list,if the element is null,then will throw a nullpointerException
		//String joinedStr = Joiner.on(",").join(list);
		
		//skipNull when the element is null,use the method skipNulls.
		String joinedStrSkipNull = Joiner.on(",").skipNulls().join(list);
		System.out.println(joinedStrSkipNull);
    }
    
	@Test
	//replace null with specified value
	public void testUserForNull(){
		List<String> list = new ArrayList<String>();
		list.add(null);
		list.add("a");
		list.add(null);
		list.add("b");
		list.add("c");
		String joinedReplaceNull = Joiner.on("|").useForNull("missing").join("foo","bar","fad",null);
		System.out.println(joinedReplaceNull);
	}
	
	@Test
	//append some element to  StringBuilder by joiner
	public void testAppendToStringBuilder(){
		StringBuilder sb = new StringBuilder();
		Joiner.on("|").skipNulls().appendTo(sb,"foo","bar","baz",null);
		System.out.println(sb);
	}
	
	@Test
	//append some elements to appendable infterface by joiner
	 public void testAppendToAppendable() throws IOException{
		   FileWriter fileWriter = new FileWriter(new File("/home/zhou/tmp/appendToAppendable.tmp"));
			Date now = new Date();
			List<Date> dateList = new ArrayList<Date>();
		   dateList.add(now);
		   dateList.add(now);
		   dateList.add(now);
		   dateList.add(now);
		   dateList.add(now);
		   dateList.add(now);
		   dateList.add(now);
			Joiner joiner = Joiner.on("#").useForNull(" ");
			//returns the FileWriter instance with the values appended into it
			joiner.appendTo(fileWriter,dateList);
			fileWriter.flush();
	}
	
	@Test
	public void testMapJoiner(){
		      //Using LinkedHashMap so that the original order is preserved
				//Map<String,String> testMap = Maps.newLinkedHashMap();
				Map<String,String> testMap = new HashMap<String,String>();
				testMap.put("Washington D.C","Redskins");
				testMap.put("New York City","Giants");
				testMap.put("Philadelphia","Eagles");
				testMap.put("Dallas","Cowboys");
				String returnedString = Joiner.on("#").
				withKeyValueSeparator("=").join(testMap);
				System.out.println(returnedString);
	}
	
	@Test
	public void testArrayJoiner(){
		Date[] arr = new Date[]{new Date(),new Date(),new Date()};
		//the joiner with invoke the element's toString method to get a string an  concatenate.
		System.out.println(Joiner.on(",").join(arr));
	}
}
