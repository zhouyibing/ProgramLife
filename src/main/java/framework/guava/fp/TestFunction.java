package framework.guava.fp;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class TestFunction{

	@Test
	public void testForMap(){
		// the maps like a map object create factory
		Map<String,String> map = Maps.newLinkedHashMap();
		map.put("YC", "0795,赣C");
		map.put("NC", "0791,赣A");
		map.put("FZ", "0792,赣B");
		Function<String, String> lookup = Functions.forMap(map, null);
		System.out.println(lookup.apply("YC"));
	}
	
	@Test
	public void testCompose(){
		Map<String,String> map = Maps.newLinkedHashMap();
		map.put("YC", "0795,赣C");
		map.put("NC", "0791,赣A");
		map.put("FZ", "0792,赣B");
		Function<String, String> mapf = Functions.forMap(map, null);
		Function<String,String> carPrefix = new GainCarPrefix();
		Function<String,String> composed = Functions.compose(carPrefix, mapf);
		System.out.println(composed.apply("YC"));
	}
	
	static class GainCarPrefix implements Function<String,String>{

		@Override
		public String apply(String input) {
			Preconditions.checkNotNull(input);
			List<String> eles =  Splitter.on(",").omitEmptyStrings().splitToList(input);
			Preconditions.checkPositionIndex(1, eles.size());
			return eles.get(1);
		}
	}
}
