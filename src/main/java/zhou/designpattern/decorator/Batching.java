package zhou.designpattern.decorator;

/**
 * Created by Zhou Yibing on 2016/3/3.
 * 配料
 */
public abstract class Batching extends FiredItem{
         FiredItem main;

        public Batching(FiredItem main) {
                this.main = main;
        }

        public abstract String getDescription();
}
