package framework.zookeeper.curator.model;

import framework.zookeeper.ZookeeperOperator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;

import java.lang.reflect.Constructor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by Zhou Yibing on 2015/11/3.
 */
public class SuperCuratorTest {
    protected static Log log = LogFactory.getLog(SuperCuratorTest.class.getName());

    private ExecutorService executorService;
    private CuratorFramework curatorFramework;
    private CountDownLatch countDownLatch;

    public SuperCuratorTest(ExecutorService executorService, CuratorFramework curatorFramework, CountDownLatch countDownLatch) {
        this.executorService = executorService;
        this.curatorFramework = curatorFramework;
        this.countDownLatch = countDownLatch;
    }

    public <T extends Work> void start(Class<T> work,Object[] params) throws Exception{
        int len = (null==params)?0:params.length;
        curatorFramework.start();
        log.info("client started!");
        Object[] conParams = new Object[3+len];
        Class[] conParamTypes = new Class[3+len];
        conParams[0]=countDownLatch;
        conParamTypes[0]=CountDownLatch.class;
        conParamTypes[1]=String.class;
        conParams[2]=curatorFramework;
        conParamTypes[2]=CuratorFramework.class;
        if(null!=params){
            for(int j=3;j<conParams.length;j++){
                conParams[j]=params[j-3];
                conParamTypes[j]=params[j-3].getClass();
            }
        }
        for(int i=0;i<countDownLatch.getCount();i++){
            Constructor<Work> con = (Constructor<Work>) work.getConstructor(conParamTypes);
            conParams[1]="client"+i;
            Work w = con.newInstance(conParams);
            executorService.submit(w);
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("All the task has completed!");
        CloseableUtils.closeQuietly(curatorFramework);
        log.info("client closed!");
    }
}
