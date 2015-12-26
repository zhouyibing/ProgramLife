package framework.guava.utilities;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.sun.istack.internal.Nullable;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * 
 *Ordering实例就是一个特殊的Comparator实例。Ordering把很多基于Comparator的静态方法（如Collections.max）包装为自己的实例方法（非静态方法），并且提供了链式调用方法，来定制和增强现有的比较器。
方法	描述
natural()	对可排序类型做自然排序，如数字按大小，日期按先后排序
usingToString()	按对象的字符串形式做字典排序[lexicographical ordering]
from(Comparator)	把给定的Comparator转化为排序器
 */
public class TestOrdering {

	@Test
	public void testDefineOrdering(){
		Ordering<String> byLengthOrdering = new Ordering<String>() {
			  public int compare(String left, String right) {
			    return Ints.compare(left.length(), right.length());
			  }
			};
			List<String> list = Lists.newArrayList("a","aaaaaaa","aaaa","a","aa");
			list = byLengthOrdering.sortedCopy(list);
			System.out.println(Joiner.on(",").join(list));
	}
	
	@Test
	public void testOrderingWithFunction(){
		//onResultOf(Function) 对集合中元素调用Function，再按返回值用当前排序器排序。
		//nullsFirst() 使用当前排序器，但额外把null值排到最前面。
		Ordering<Foo> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Foo, String>() {//每次链式调用都是用后面的方法包装了前面的排序器
			  public String apply(Foo foo) {
			    return foo.sortedBy;
			  }
			});
		Foo foo1 = new Foo("a",1);
		Foo foo2 = new Foo("b",2);
		assertThat(ordering.compare(foo1, foo2),is(-1));
		assertThat(foo1, is(ordering.min(foo1, foo2)));//返回两个参数中最小的那个。如果相等，则返回第一个参数。
		assertThat(foo2, is(ordering.max(foo1, foo2)));
		List<Integer> list = Lists.newArrayList(1,2,3,4,5,5,5,4,6,6,7,78,8);
		//Ordering<Integer>ordering2 = Ordering.usingToString().nullsFirst();//8,78,7,6
		Ordering<Integer>ordering2 = Ordering.natural().nullsFirst();
		List<Integer> list2 = ordering2.greatestOf(list,4);//获取可迭代对象中最大的k(k=4)个元素。
		System.out.println(Joiner.on(",").skipNulls().join(list2));
	}
	
	class Foo {
	    @Nullable String sortedBy;
	    int notSortedBy;
		public Foo(String sortedBy, int notSortedBy) {
			super();
			this.sortedBy = sortedBy;
			this.notSortedBy = notSortedBy;
		}
	}
}
