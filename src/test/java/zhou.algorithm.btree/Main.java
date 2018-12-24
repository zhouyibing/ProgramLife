package zhou.algorithm.btree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        List<String> a = new ArrayList<>();
        Map<String,String> m = new HashMap<>();
        for(int i=0;i<10;i++) {
            a.add(i + "");
            m.put(i+"",i+"");
        }
        a.add("dd");
        m.put("dd","dd");
        a.size();
        m.size();
    }
}
