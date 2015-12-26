package framework.guava.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ItalicAction;

import org.junit.Test;
import static  org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;

public class TestCollections {

	@Test
	//ClassToInstanceMap是一种特殊的Map：它的键是类型，而值是符合键所指类型的对象。
	public void testClassToInstanceMap(){
		ClassToInstanceMap<Number> classToInstanceMap = MutableClassToInstanceMap.create();
		ClassToInstanceMap<Object> classToInstanceMap2 = ImmutableClassToInstanceMap.builder().build();
		try{
		classToInstanceMap2.put(Integer.class, 1);
		classToInstanceMap2.put(Integer.class, 2);
		classToInstanceMap2.put(Integer.class, 3);
		classToInstanceMap2.remove(Integer.class);
		assertThat(classToInstanceMap2.size(),is(0));
		}catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
		
		classToInstanceMap.put(Double.class, 22d);
		classToInstanceMap.put(Double.class, 23d);
		classToInstanceMap.put(Double.class, 24d);
		assertThat(classToInstanceMap.get(Double.class),is((Number)24d));
		assertThat(classToInstanceMap.getInstance(Double.class),is((Number)24d));
		
		classToInstanceMap.remove(Double.class);
		assertThat(classToInstanceMap.get(Double.class),is(nullValue()));
	}
	
	@Test
	//RangeSet描述了一组不相连的、非空的区间。当把一个区间添加到可变的RangeSet时，所有相连的区间会被合并，空区间会被忽略。
	public void testRangeSet(){
		RangeSet<Integer> rangeSet = TreeRangeSet.create();
		rangeSet.add(Range.closed(1, 10)); // {[1,10]}
		System.out.println(rangeSet);
		rangeSet.add(Range.closedOpen(11, 15));//不相连区间:{[1,10], [11,15)}
		System.out.println(rangeSet);
		rangeSet.add(Range.closedOpen(15, 20)); //相连区间; {[1,10], [11,20)}
		System.out.println(rangeSet);
		rangeSet.add(Range.openClosed(0, 0)); //空区间; {[1,10], [11,20)}
		System.out.println(rangeSet);
		rangeSet.remove(Range.open(5, 10)); //分割[1, 10]; {[1,5], [10,10], [11,20)}
		System.out.println(rangeSet);
		
		assertThat(rangeSet.contains(2),is(true));//RangeSet最基本的操作，判断RangeSet中是否有任何区间包含给定元素。
		assertThat(rangeSet.rangeContaining(11),is(Range.closedOpen(11, 20)));//返回包含给定元素的区间；若没有这样的区间，则返回null。
		assertThat(rangeSet.encloses(Range.closedOpen(11, 20)), is(true));//判断RangeSet中是否有任何区间包括给定区间。
		assertThat(rangeSet.span(),is(Range.closedOpen(1, 20)));//返回包括RangeSet中所有区间的最小区间。
	}
	
	@Test
	//RangeMap描述了”不相交的、非空的区间”到特定值的映射。和RangeSet不同，RangeMap不会合并相邻的映射，即便相邻的区间映射到相同的值。
	public void testRangeMap(){
		RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
		rangeMap.put(Range.closed(1, 10), "foo"); //{[1,10] => "foo"}
		rangeMap.put(Range.open(3, 6), "bar"); //{[1,3] => "foo", (3,6) => "bar", [6,10] => "foo"}
		rangeMap.put(Range.open(10, 20), "foo"); //{[1,3] => "foo", (3,6) => "bar", [6,10] => "foo", (10,20) => "foo"}
		rangeMap.remove(Range.closed(5, 11)); //{[1,3] => "foo", (3,5) => "bar", (11,20) => "foo"}
		assertThat(rangeMap.get(2),is("foo"));
		
		Map<Range<Integer>,String> rm = rangeMap.asMapOfRanges();//用Map<Range<K>, V>表现RangeMap。这可以用来遍历RangeMap。
		System.out.println(rm);
	    assertThat(rm.get(Range.open(11, 20)),is("foo"));
	   System.out.println( rangeMap.subRangeMap(Range.closed(12, 15)));//用RangeMap类型返回RangeMap与给定Range的交集视图。这扩展了传统的headMap、subMap和tailMap操作。
	}
}
