package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 炒类
 */
public abstract class FiredItem {

    String description;
    abstract double cost();
    String  getDescription(){
        return description;
    }
}
