package framework.ehcache;

import framework.quartz.util.UUID;
import org.apache.log4j.PropertyConfigurator;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.annotations.BeforeTest;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhou on 4/9/16.
 */
public class EhcacheExample {
    private static Cache<String,Session> sessionCache;
    private static CacheManager cacheManager;
    private Cache<Long,String> testCache;
    @Before
    public  void init(){
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("test", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,String.class, ResourcePoolsBuilder.heap(10))).build();
        cacheManager.init();
        testCache = cacheManager.getCache("test",Long.class,String.class);

        sessionCache = cacheManager.createCache("session",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,Session.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(1, MemoryUnit.GB)).build());
    }

    @Test
    public void testCopy(){
        Session s1 = createSession();
        Session s2 = createSession();

        sessionCache.put(s1.getId(),s1);
        sessionCache.put(s2.getId(),s2);
        Cache<String,Session> cache2 = sessionCache;
    }

    /**
     * -XX:MaxDirectMemorySize=1024MB
     */
    public static void main(String[] args){
        EhcacheExample ehcacheExample = new EhcacheExample();
        for(int i=0;i<30;i++){
            Session s = ehcacheExample.createSession();
            sessionCache.put(s.getId(),s);
            System.out.println(i+":put session--->"+s.getId());
        }
        int seq=0;
        for(Iterator<Cache.Entry<String,Session>> it = sessionCache.iterator(); it.hasNext();){
            Cache.Entry<String,Session> entry = it.next();
            System.out.println((++seq)+":key="+entry.getKey()+",value="+entry.getValue());
        }
        cacheManager.close();
    }

    @Test
    public void testConcurrentModify(){
        //final Session s = createSession();
        //sessionCache.put(s.getId(),s);
        //System.out.println("before:"+s);
        long a=1;
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i =0;i<10;i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    testCache.put(1L,finalI+"");
                }
            });
        }

        pool.shutdown();
        //System.out.println("after:"+sessionCache.get(s.getId()));
    }

    public Session createSession(){
        Session s = new Session();
        s.setId(UUID.randomUUID().toString());
        s.setLastActiveTime(System.currentTimeMillis());
        s.setUserId(UUID.randomUUID().toString());
        s.setVid(UUID.randomUUID().toString());
        return s;
    }

    static class Session implements Serializable {
        private String id;
        private long lastActiveTime;
        private String vid;
        private String userId;
        private int verNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getLastActiveTime() {
            return lastActiveTime;
        }

        public void setLastActiveTime(long lastActiveTime) {
            this.lastActiveTime = lastActiveTime;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getVerNo() {
            return verNo;
        }

        public void setVerNo(int verNo) {
            this.verNo = verNo;
        }

        @Override
        public String toString() {
            return "Session{" +
                    "id='" + id + '\'' +
                    ", lastActiveTime=" + lastActiveTime +
                    ", vid='" + vid + '\'' +
                    ", userId='" + userId + '\'' +
                    ", verNo=" + verNo +
                    '}';
        }
    }
}
