package framework.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;

/**
 * Created by Zhou Yibing on 2016/3/1.
 */
public class MongodbSimpleTest {

    public static void main(String[] args) throws ParseException {
         MongoClient mongoClient = new MongoClient("192.168.80.142");
        //1.使用数据库
        MongoDatabase db = mongoClient.getDatabase("test");
        //2.得到collection
        MongoCollection restaurants  = db.getCollection("restaurants");
        //query(restaurants);
        //insert(restaurants);

        //delete(restaurants);
        //update(restaurants);
        //replace(restaurants);
        FindIterable findIterable = restaurants.find(eq("restaurant_id", "40361606"));
        findIterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document o) {
                System.out.println(o);
            }
        });

        MongoDatabase db2 = mongoClient.getDatabase("aaa");
        //db2.createCollection("ccc");
        //db2.getCollection("ccc").drop();
        //db2.drop();

        //groupQuery(restaurants);
        matchAndGroup(restaurants);
        createIndex(restaurants);
        mongoClient.close();
    }

    public static void createIndex(MongoCollection restaurants){
        restaurants.dropIndex(new Document("restaurant_id",1));
        String result = restaurants.createIndex(new Document("restaurant_id",1).append("name",1));
        System.out.println(result);
    }

    /**
     * 根据borough字段分组,并对分组进行计数
     * @param restaurants
     */
    public static void groupQuery(MongoCollection restaurants){
         AggregateIterable iterable = restaurants.aggregate(asList(new Document("$group", new Document("_id", "$borough").append("count", new Document("$sum", 1)))));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document o) {
                System.out.println(o);
            }
        });
    }

    public static void matchAndGroup(MongoCollection restaurants){
        AggregateIterable iterable = restaurants.aggregate(asList(new Document("$match",new Document("borough","Queens").append("cuisine","Brazilian")),new Document("$group",new Document("_id","$address.zipcode").append("count",new Document("$sum",1)))));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document o) {
                System.out.println(o);
            }
        });
    }

    public static void update(MongoCollection restaurants){
         UpdateResult  updateResult = restaurants.updateMany(eq("restaurant_id", "41704620"), new Document("$set", new Document("street", "NanJing rd"))
                 .append("$set", new Document("zipCode", "201700"))
                 .append("$currentDate", new Document("lastModified", true)));
        System.out.println("match count:"+updateResult.getMatchedCount()+",modified count:"+updateResult.getModifiedCount());
    }

    public static void replace(MongoCollection restaurants){
        UpdateResult  updateResult = restaurants.replaceOne(eq("restaurant_id", "40361606"),new Document("aa","11").append("ccc", "33").append("restaurant_id","40361606"));
        System.out.println("match count:"+updateResult.getMatchedCount()+",modified count:"+updateResult.getModifiedCount());
    }

    public static void delete(MongoCollection restaurants){
       DeleteResult deleteResult =  restaurants.deleteMany(eq("restaurant_id", "41704620"));
        System.out.println("deletedCount:"+deleteResult.getDeletedCount());
    }

    public static void insert(MongoCollection restaurants) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        restaurants.insertOne(
                        new Document("address",
                                new Document()
                                        .append("street", "2 Avenue")
                                        .append("zipcode", "10075")
                                        .append("building", "1480")
                                        .append("coord", asList(-73.9557413, 40.7720266)))
                                .append("borough", "Manhattan")
                                .append("cuisine", "Italian")
                                .append("grades", asList(
                                        new Document()
                                                .append("date", format.parse("2014-10-01T00:00:00Z"))
                                                .append("grade", "A")
                                                .append("score", 11),
                                        new Document()
                                                .append("date", format.parse("2014-01-16T00:00:00Z"))
                                                .append("grade", "B")
                                                .append("score", 17)))
                                .append("name", "Vella")
                        .append("restaurant_id", "41704620"));
    }

    public static void query(MongoCollection restaurants){
        //3.设置查询条件,查询数据
        FindIterable<Document> results = null;
        //results= restaurants.find(new Document("borough", "Manhattan"));
        //results = restaurants.find(eq("borough","Brooklyn"));
        //results = restaurants.find(eq("address.zipcode","11208"));
        //results = restaurants.find(in("grades.grade",new String[]{"A","B"}));
        //将查询结果进行排序 1 for ascending and -1 for descending.
        results = restaurants.find(eq("borough","Brooklyn")).sort(new Document("borough",1).append("address.zipcode",-1));
        results.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document);
            }
        });
    }
}
