package framework.openhft;

import net.openhft.collections.SharedHashMap;
import net.openhft.collections.SharedHashMapBuilder;
import net.openhft.collections.SharedMapEventListener;
import net.openhft.lang.io.Bytes;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhou on 4/9/16.
 */
public class TestSharedHashMap {

    public static void main(String[] args){

    }

    public static SharedHashMap<String,String> testSharedHashMap() throws IOException {
        SharedHashMap<String,String> map= SharedHashMapBuilder.of(String.class,String.class)
                .file(new File("/home/Documents/sharedmap")).entrySize(10)
                .eventListener(new SharedMapEventListener() {
                    @Override
                    public void onRemove(SharedHashMap map, Bytes entry, int metaDataBytes, Object key, Object value) {
                        super.onRemove(map, entry, metaDataBytes, key, value);
                    }

                    @Override
                    public Object onGetMissing(SharedHashMap map, Bytes keyBytes, Object key, Object usingValue) {
                        return super.onGetMissing(map, keyBytes, key, usingValue);
                    }

                    @Override
                    public void onGetFound(SharedHashMap map, Bytes entry, int metaDataBytes, Object key, Object value) {
                        super.onGetFound(map, entry, metaDataBytes, key, value);
                    }

                    @Override
                    public void onPut(SharedHashMap map, Bytes entry, int metaDataBytes, boolean added, Object key, Object value) {
                        super.onPut(map, entry, metaDataBytes, added, key, value);
                    }
                })
                .create();
        return map;
    }
}
