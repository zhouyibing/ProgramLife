package framework.btrace;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

/**
 * Created by Zhou Yibing on 2015/12/8.
 */
@BTrace
public class RequestMonitor {
    @TLS
    private static long startTime=0;

    @OnMethod(
            clazz ="/com\\.manyi\\.iw\\.agent\\.soa\\.server\\.web\\.controller\\.sale\\..*/",
            method="/.*/",
            location = @Location(Kind.ENTRY)
    )
    public static void start(){
        startTime= BTraceUtils.timeMillis();
    }

    @OnMethod(
            clazz = "/com\\.manyi\\.iw\\.agent\\.soa\\.server\\.web\\.controller\\.sale\\..*/",
            method = "/.*/",
            location=@Location(Kind.RETURN)
    )
    public static void end(){
        long time =timeMillis()-startTime;
        println(strcat(strcat(strcat(strcat(name(probeClass()), "#"), probeMethod()), " execute time(millis):"), str(time)));
    }
}
