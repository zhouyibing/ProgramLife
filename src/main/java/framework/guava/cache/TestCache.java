package framework.guava.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import static org.junit.Assert.assertThat;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.Callables;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

public class TestCache {

	@Test
	/**
	 * Here we are creating ConcurrentMap with String keys and Book objects for
the values (specified by the generics on the ConcurrentMap declaration). Our
first method call, concurrencyLevel(), sets the amount of concurrent modifications
we will allow in the map. We've also specified the softValues() method so
the values from the map are each wrapped in a SoftReference object and may
be garbage-collected if the memory becomes low. Other options we could have
specified include weakKeys() and weakValues(), but there is no option for using
softKeys(). When using WeakReferences or SoftReferences for either keys or
values, if one is garbage-collected, the entire entry is removed from the map; partial
entries are never exposed to the client.
	 */
	public void testMapMaker(){
		ConcurrentMap<String,String> books = new MapMaker().concurrencyLevel(2).softValues().makeMap();
	}
	
	@Test
	public void testCache(){
		
		Callable<String> value = Callables.returning("Foo");
		//只允许两个线程并发修改，存储的value为软引用。
		Cache<String, String> cache = CacheBuilder.newBuilder().concurrencyLevel(2).softValues().build();
		cache.put("a", "a");
		try {
			//value为当获取key（a）没有找到时，返回的默认值
			System.out.println(cache.get("a",value));
			//getIfPresent表示如果存在key则返回值，不存在就返回null
			System.out.println(cache.getIfPresent("b"));
			cache.invalidate("a");//让key为a的value失效，即清除值（置为null）调用的是segment的remove操作
			cache.invalidateAll();//让所有的key失效
			System.out.println(cache.getIfPresent("a"));
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoadingCache() throws Exception{
		CacheLoader<String, String> cacheLoad = mock(CacheLoader.class);
		when(cacheLoad.load("1")).thenReturn("a");//模拟方法调用的返回值；当调用cacheLoad.load("1")时返回a
		LoadingCache<String, String> tradeAccountCache = CacheBuilder.newBuilder().maximumSize(5000L).expireAfterAccess(1,TimeUnit.SECONDS) .build(cacheLoad);
		assertThat(tradeAccountCache.get("1"), is("a"));
		when(cacheLoad.load("2")).thenReturn("b");
		tradeAccountCache.refresh("1");
		assertThat(tradeAccountCache.get("2"), is("b"));
		Thread.sleep(1000);
		assertThat(tradeAccountCache.get("1"), is("a"));
		verify(cacheLoad,times(1)).load("1");
	}
}
