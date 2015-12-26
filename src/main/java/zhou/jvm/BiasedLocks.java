package zhou.jvm;
import java.util.concurrent.locks.LockSupport;

/**
 * 触发STW暂停最常见的原因就是垃圾回收了（github中的一个例子），但不同的JIT活动(例子)，偏向锁擦除（例子），特定的JVMTI操作，以及许多场景也可能会导致应用程序暂停。
 *
 ***
 */
public class BiasedLocks {

    private static synchronized void contend() {
        LockSupport.parkNanos(100000);
    }

    // Run with: -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDetails
    // Notice that there are a lot of stop the world pauses, but no actual garbage collections
    // This is because PrintGCApplicationStoppedTime actually shows all the STW pauses

    // To see what's happening here, you may use the following arguments:
    // -XX:+PrintSafepointStatistics  -XX:PrintSafepointStatisticsCount=1
    // It will reveal that all the safepoints are due to biased lock revocations.

    // Biased locks are on by default, but you can disable them by -XX:-UseBiasedLocking
    // It is quite possible that in the modern massively parallel world, they should be
    // turned back off by default

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(5000); // Because of BiasedLockingStartupDelay

        /*Stream.generate(() -> new Thread(BiasedLocks::contend))
                .limit(10)
                .forEach(Thread::start);*/
    }

}