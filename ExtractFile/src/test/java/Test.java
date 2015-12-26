import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhou Yibing on 2015/12/18.
 */
public class Test {

    public static void main(String[] args){
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        List<String> subList  = list.subList(0,3);
        subList.remove(1);
        for(String item :list){
            System.out.println(item);
        }
    }
}
