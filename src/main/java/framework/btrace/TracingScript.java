package framework.btrace;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

/**
 * Created by Zhou Yibing on 2015/12/4.
 */
@BTrace
public class TracingScript {

    private static Object totalCount = 0;
    @OnMethod(
            clazz="framework.btrace.Counter",
            method="add",
            location=@Location(Kind.RETURN)
    )
    //获取add()方法参数值和返回值。
    public static void getParamAndResult(int num,@Return int result){
        println("===========================");
        println(strcat("parameter num:",str(num)));
        println(strcat("retrun value:",str(result)));
    }

    @OnMethod(
            clazz="framework.btrace.Counter",
            method="add",
            location=@Location(Kind.RETURN)
    )
    public static void getTotalCount(@Self framework.btrace.Counter counter){
        totalCount = get(field("framework.btrace.Counter", "totalCount"), counter);
    }

    //获得add方法执行时间
    @TLS private static long startTime=0;

    @OnMethod(
            clazz="framework.btrace.Counter",
            method="add"
    )
    public static void startExecute(){
        startTime=timeNanos();
    }

    @OnMethod(
            clazz="framework.btrace.Counter",
            method="add",
            location=@Location(Kind.RETURN)
    )
    public static void endExecute(@Duration long duration){
           long time =timeNanos()-startTime;
           println(strcat("execute time(nanos):",str(time)));
           println(strcat("duration(nanos):",str(duration)));
    }

    @OnTimer(1000)
    public static void print(){
        println("=====================");
        println(strcat("totalCount:",str(totalCount)));
    }

    private static long count;
    @OnMethod(
            clazz = "/.*/",
            method="add",
            location = @Location(value=Kind.CALL,clazz = "/.*/",method = "sleep")
    )
    public static void invokeSleepCount(@ProbeClassName String pcm,@ProbeMethodName String pmm,@TargetInstance Object instance,@TargetMethodOrField String method){
        println("================= ");
        println(strcat("ProbeClassName: ",pcm));
        println(strcat("ProbeMethodName: ",pmm));
        println(strcat("TargetInstance: ",str(classOf(instance))));
        println(strcat("TargetMethodOrField : ",str(method)));
        count++;
    }

    @OnEvent
    public static void printCount(){
        println(strcat("count:",str(count)));
    }
}
