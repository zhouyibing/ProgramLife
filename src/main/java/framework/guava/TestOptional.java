package framework.guava;

import java.util.Set;
import org.junit.Test;
import com.google.common.base.Optional;
import static org.junit. Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestOptional {

	@Test
	public void testOptional() {
		try{
		Optional<Integer> op = Optional.of(null);//创建指定引用的Optional实例，若引用为null则快速失败
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		Optional<Integer> op3 = Optional.absent();//创建引用缺失的Optional实例
		try{
	    System.out.println(op3.get());//返回Optional所包含的引用，若引用缺失，则抛出java.lang.IllegalStateException
		}catch (IllegalStateException e) {
		  e.printStackTrace();
		}
		assertThat(op3.isPresent(),is(false));//如果Optional包含非null的引用（引用存在），返回true
		assertThat(op3.or(3),is(3));//返回Optional所包含的引用，若引用缺失，返回指定的值
		assertThat(op3.orNull(),is(nullValue()));//返回Optional所包含的引用，若引用缺失，返回null
		Optional<Integer> op2 = Optional.of(2);
		assertThat(op2.get(),is(2));
		Set<Integer> op2Set= op2.asSet();//返回Optional所包含引用的单例不可变集，如果引用存在，返回一个只有单一元素的集合，如果引用缺失，返回一个空集合。
		assertThat(op2Set.size(),is(1));
		assertThat(op3.asSet().isEmpty(),is(true));
	}

}
