package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 */
public class Main {

    public static void main(String[] args){
        FiredItem firedRice = new FiredRice();
        firedRice=new Egg(firedRice);//+egg
        firedRice = new Sausage(firedRice);//+sausage
        System.out.println(firedRice.getDescription());
        System.out.println("总价格:"+firedRice.cost());

        FiredItem firedNoddle = new FiredNoddle();
        firedNoddle = new Sausage(firedNoddle);
        System.out.println(firedNoddle.getDescription());
        System.out.println("总价格:"+firedNoddle.cost());
    }
}
