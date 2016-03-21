package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class FiredRice extends FiredItem{
    public FiredRice() {
        description = "炒饭";
    }

    public double cost() {
        return 6.0;
    }
}
