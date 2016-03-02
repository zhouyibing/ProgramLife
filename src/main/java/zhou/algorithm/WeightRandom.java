package zhou.algorithm;


import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Zhou Yibing on 2016/3/2.
 * 权重随机算法的实现,可应用于抽奖
 */
public class WeightRandom {
    private int total;//总值
    private GoodsWeight[] goodsWeights;
    private Map<GoodsWeight,ValueRange> goodsWeightValueRangeMap=new HashMap<GoodsWeight,ValueRange>();

    static class ValueRange{
        private int start;
        private int end;

        public ValueRange(int start, int end) {
            if(start<0) start = 0;
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public boolean isInRange(int num){
            return num>start&&num<=end;
        }
    }

    public WeightRandom(GoodsWeight[] goodsWeights, int total) {
        this.goodsWeights = goodsWeights;
        this.total = total;
        int remain = total;
        double totalWeight = 0;
        for(GoodsWeight goodsWeight:goodsWeights){
            totalWeight+=goodsWeight.getWeight();
        }
        if(totalWeight>1||totalWeight<=0) throw new IllegalArgumentException("所有物品权重和不能大于1或小于0");
        for(GoodsWeight goodsWeight:goodsWeights){
            int weightValue = (int) (goodsWeight.getWeight()*total);
            remain = remain - weightValue;
            ValueRange valueRange = new ValueRange(total-remain-weightValue,total-remain);
            goodsWeightValueRangeMap.put(goodsWeight,valueRange);
        }
    }

    public GoodsWeight random(){
        int ran = ThreadLocalRandom.current().nextInt(total);

        for(GoodsWeight goodsWeight:goodsWeights){
            ValueRange valueRange = goodsWeightValueRangeMap.get(goodsWeight);
            if(valueRange.isInRange(ran)){
                return goodsWeight;
            }
        }
        return null;
    }

    public static void main(String[] args){
        GoodsWeight[] goodsWeights = new GoodsWeight[4];
        goodsWeights[0]=new GoodsWeight("goods-1",0.1);
        goodsWeights[1]= new GoodsWeight("goods-2",0.7);
        goodsWeights[2]= new GoodsWeight("goods-3",0.1);
        goodsWeights[3]= new GoodsWeight("goods-4",0.1);
        Map<GoodsWeight,Integer> countMap = new HashMap<GoodsWeight,Integer>();
        countMap.put(goodsWeights[0],0);
        countMap.put(goodsWeights[1],0);
        countMap.put(goodsWeights[2],0);
        countMap.put(goodsWeights[3],0);
         WeightRandom weightRandom = new WeightRandom(goodsWeights,100);
        for(int i=0;i<10000;i++) {
            GoodsWeight goodsWeight = weightRandom.random();
            if(null!=goodsWeight) {
                Integer count = countMap.get(goodsWeight);
                count++;
                countMap.put(goodsWeight, count);
            }
        }

        for(Map.Entry<GoodsWeight,Integer> enty:countMap.entrySet()){
            System.out.println(enty.getKey().getGoodsName()+"'s count:"+enty.getValue());
        }
    }

    static class GoodsWeight{
        private String goodsName;
        private double weight;

        public GoodsWeight(String goodsName, double weight) {
            this.goodsName = goodsName;
            this.weight = weight;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
}
