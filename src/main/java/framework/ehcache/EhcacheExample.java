package framework.ehcache;

import framework.quartz.util.UUID;
import org.apache.log4j.PropertyConfigurator;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by zhou on 4/9/16.
 */
public class EhcacheExample {

    /**
     * -XX:MaxDirectMemorySize=1024MB
     */
    public static void main(String[] args){
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("test", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,String.class, ResourcePoolsBuilder.heap(10))).build();
        cacheManager.init();
        Cache<Long,String> testCache = cacheManager.getCache("test",Long.class,String.class);
        testCache.put(1L,"a");
        testCache.put(1L,"b");
        testCache.put(2L,"b");
        System.out.println(testCache.get(1L));
        cacheManager.removeCache("test");

        Cache<String,Session> sessionCache = cacheManager.createCache("session",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,Session.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10,  EntryUnit.ENTRIES)/*.offheap(20,MemoryUnit.GB)*/).build());
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

        @Override
        public String toString() {
            return "Session{" +
                    "id='" + id + '\'' +
                    ", lastActiveTime=" + lastActiveTime +
                    ", vid='" + vid + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }
}
