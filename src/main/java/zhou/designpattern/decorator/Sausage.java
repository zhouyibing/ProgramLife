package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class Sausage extends Batching{
    public Sausage(FiredItem main) {
        super(main);
    }

    @Override
    double cost() {
        return main.cost()+2;
    }

    @Override
    public String getDescription() {
        return main.getDescription()+"+香肠";
    }
}
