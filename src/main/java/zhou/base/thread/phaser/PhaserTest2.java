package zhou.base.thread.phaser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;
/**
 * 2.1 Registration
    Phaser支持通过register()和bulkRegister(int parties)方法来动态调整注册任务的数量，此外也支持通过其构造函数进行指定初始数量。
    在适当的时机，Phaser支持减少注册任务的数量，例如 arriveAndDeregister()。单个Phaser实例允许的注册任务数的上限是65535。

2.2 Arrival
    正如Phaser类的名字所暗示，每个Phaser实例都会维护一个phase number，初始值为0。
    每当所有注册的任务都到达Phaser时，phase number累加，并在超过Integer.MAX_VALUE后清零。
    arrive()和arriveAndDeregister()方法用于记录到 达，arriveAndAwaitAdvance()方法用于记录到达，并且等待其它未到达的任务。

2.3 Termination
    Phaser支持终止。Phaser终止之后，调用register()和bulkRegister(int parties)方法没有任何效果，
    arriveAndAwaitAdvance()方法也会立即返回。触发终止的时机是在protected boolean onAdvance(int phase, int registeredParties)方法返回时
    ，如果该方法返回true，那么Phaser会被终止。默认实现是在注册任务数为0时返回true（即 return registeredParties == 0;）。
    此外，forceTermination()方法用于强制终止，isTerminated()方法用于判断是否已经终止。

2.4 Tiering
    Phaser支持层次结构，即通过构造函数Phaser(Phaser parent)和Phaser(Phaser parent, int parties)构造一个树形结构。
    这有助于减轻因在单个的Phaser上注册过多的任务而导致的竞争，从而提升吞吐量，代价是增加单个操作的开销。
 * @author zhou
 *
 */
public class PhaserTest2 {

    public static void main(String args[]) throws Exception {
        //
        final Phaser phaser = new Phaser(1);
        for(int i = 0; i < 5; i++) {
            phaser.register();
            System.out.println("starting thread, id: " + i);
            final Thread thread = new Thread(new Task(i, phaser));
            thread.start();
        }
        
        //
        System.out.println("Press ENTER to continue");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();
        phaser.arriveAndDeregister();
    }
    
    public static class Task implements Runnable {
        //
        private final int id;
        private final Phaser phaser;

        public Task(int id, Phaser phaser) {
            this.id = id;
            this.phaser = phaser;
        }
        
        @Override
        public void run() {
            phaser.arriveAndAwaitAdvance();
            System.out.println("in Task.run(), phase: " + phaser.getPhase() + ", id: " + this.id);
        }
    }
}