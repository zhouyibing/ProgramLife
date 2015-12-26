package framework.guava.utilities;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class TestSplitter {

	@Test
	public void testSplitter(){
		Iterable<String> elements = Splitter.on('|').split("foo|bar|baz");
		printIterable(elements);
		System.out.println();
		
		List<String> elements2  = Splitter.on("\\d+").splitToList("1232432as d 3 21");
		printList(elements2);
		System.out.println();
		
		//Splits on '|' and removes any leading or trailing whitespace
		Iterable<String> elements3  = Splitter.on('|').trimResults().split("sdf  ds|dsf|s   df| | |    ");
		printIterable(elements3);
	}

	private void printIterable(Iterable<String> elements){
		for(String ele:elements){
			System.out.print(ele+"#");
		}
	}
	
	private void printList(List<String> elements2){
		for(int i=0,size=elements2.size();i<size;i++)
			System.out.print(elements2.get(i)+"#");
	}
	
	@Test
	public void testMapSplitter() {
	String startString = "Washington D.C=Redskins#New York City=Giants#Philadelphia=Eagles#Dallas=Cowboys";
	Map<String,String> testMap = Maps.newLinkedHashMap();
	testMap.put("Washington D.C","Redskins");
	testMap.put("New York City","Giants");
	testMap.put("Philadelphia","Eagles");
	testMap.put("Dallas","Cowboys");
	Splitter.MapSplitter mapSplitter =
	Splitter.on("#").withKeyValueSeparator("=");
	Map<String,String> splitMap = mapSplitter.split(startString);
	System.out.println(splitMap);
	}
}
