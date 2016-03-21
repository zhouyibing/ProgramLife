package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class Egg extends Batching{
    public Egg(FiredItem main) {
        super(main);
    }

    @Override
    double cost() {
        return main.cost()+1;//价格等于主料价格+配料价格
    }

    @Override
    public String getDescription() {
        return main.getDescription()+"+鸡蛋";
    }
}
