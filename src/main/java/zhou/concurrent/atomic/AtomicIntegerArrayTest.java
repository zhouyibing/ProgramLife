package zhou.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
/**
 * Atomic包提供了三种基本类型的原子更新，但是Java的基本类型里还有char，float和double等。
 * 那么问题来了，如何原子的更新其他的基本类型呢？Atomic包里的类基本都是使用Unsafe实现的
 *Unsafe只提供了三种CAS方法，compareAndSwapObject，compareAndSwapInt和compareAndSwapLong，再看AtomicBoolean源码，
 *发现其是先把Boolean转换成整型，再使用compareAndSwapInt进行CAS，所以原子更新double也可以用类似的思路来实现。
 *
 *
 *大多数的原子类，比如AtomicLong本质上都是一个Unsafe和一个volatile Long变量的包装类
 ***
 */
public class AtomicIntegerArrayTest {

	static int[] value = new int[] { 1, 2 };

	static AtomicIntegerArray ai = new AtomicIntegerArray(value);

	public static void main(String[] args) {
		ai.getAndSet(0, 3);
		//数组value通过构造方法传递进去，然后AtomicIntegerArray会将当前数组复制一份，所以当AtomicIntegerArray对内部的数组元素进行修改时，不会影响到传入的数组。
		System.out.println(ai.get(0));
         System.out.println(value[0]);
         //原子的自增操作
         AtomicInteger ai = new AtomicInteger(1);
         System.out.println(ai.getAndIncrement());
         System.out.println(ai.get());
	}

}