package framework.guava.collections;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class TestFluentIterable {

	public static void main(String[] args) {
		List<Integer> list = Lists.newArrayList(1,2,3,4,5,5,6,78,67,4,345,23,54,32);
		//从list中过滤小于5的数，并将结果转化为Iterable
		Iterable<Integer> filteredList = FluentIterable.from(list).filter(new Predicate<Integer>() {
			@Override
			public boolean apply(Integer input) {
				return input>5;
			}
		});
		
	System.out.println(Joiner.on(",").skipNulls().join(filteredList));
	}

	@Test
	public void testTransform(){
	    long now = System.currentTimeMillis();
		List<Date> list = Lists.newArrayList(new Date(now+1),new Date(now+2),new Date(now+3));
		//将Date类型的list转化long类型的list，通过transfrom方法指定转换规则
		List<Long> dateList = FluentIterable.from(list).transform(new Function<Date, Long>() {

			@Override
			public Long apply(Date input) {
				return input.getTime();
			}
		}).toList();
		
		System.out.println(Joiner.on("|").skipNulls().join(dateList));
	}
	
}
