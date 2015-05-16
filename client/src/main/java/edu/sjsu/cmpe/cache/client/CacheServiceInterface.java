package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Cache Service Interface
 * 
 */
public interface CacheServiceInterface {
    public String get(long key);

    public void put(long key, String value);

    public int getResponseCode();

    public void delete(long key);

    public String getAllValues();

    public void asyncPut(long key, String value);

    public void asyncGet(long key, final ConcurrentHashMap<String,String> valueMap,final CountDownLatch count);


}
