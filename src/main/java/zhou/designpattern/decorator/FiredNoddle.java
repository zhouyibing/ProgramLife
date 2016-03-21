package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class FiredNoddle extends FiredItem{
    public FiredNoddle() {
        this.description = "炒面";
    }

    public double cost() {
        return 7.0;
    }
}
