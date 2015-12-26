package zhou.base.core;

import java.util.Arrays;
import java.util.List;

public class EnumTest {

	public static void main(String[] args) {
		List<String> season = Arrays.asList("SPRING","summer");
		for(int i =0;i<season.size();i++){
			Season s = Season.valueOf(season.get(i));
			if(null!=s){
				System.out.println(s);
			}else{
				System.out.println("the enum is null!");
			}
		}
	}

}
