package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sophiango on 5/15/15.
 */
public class CRDTClient {
    private Collection<CacheServiceInterface> cacheList = new ArrayList<CacheServiceInterface>();
    private final ConcurrentHashMap<String, String> getResponseValueMap = new ConcurrentHashMap<String, String>();
    private final CountDownLatch responseWaiter = new CountDownLatch(3);

    public CRDTClient(Collection<String> serverNodes) {
        Collection<String> nodes = serverNodes;
        for (String n : nodes) {
            //System.out.println(n);
            DistributedCacheService cache = new DistributedCacheService(n);
            cacheList.add(cache);
        }

    }

    public void put(long key, String value) {

        for (CacheServiceInterface cache : cacheList) {
            cache.asyncPut(Long.valueOf(key), value);
        }

        Collection<CacheServiceInterface> deleteCacheList = new ArrayList<CacheServiceInterface>();
        int count = 0;
        int responseCode=0;
        for (CacheServiceInterface cache : cacheList) {
            responseCode=cache.getResponseCode();
            if (responseCode == 200) {
                count++;
                deleteCacheList.add(cache);
            }
        }

        //System.out.println("count "+count+"deleteCacheList.count "+deleteCacheList.size());
        if (count < 2) {
            for (CacheServiceInterface cache : deleteCacheList) {
                cache.delete(key);
                System.out.println("Cache deleted");
            }
        }

    }

    public String get(long key) {
        String finalValue = null;
        String resValue = null;

        for (CacheServiceInterface cache : cacheList) {
            cache.asyncGet(key, getResponseValueMap, responseWaiter);
        }

        HashMap<String, Integer> uniqueValueMap = new HashMap<String, Integer>();

        try {
            responseWaiter.await();
            System.out.println(" After wait");
            for (String node : getResponseValueMap.keySet()) {
                System.out.println(node + " " + getResponseValueMap.get(node));
                resValue = getResponseValueMap.get(node);
                if (uniqueValueMap.containsKey(resValue)) {

                    uniqueValueMap.put(resValue,uniqueValueMap.get(resValue) + 1);
                    finalValue = resValue;
                } else {
                    uniqueValueMap.put(resValue, 1);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while waiting for countdown "+ e);
        }
        System.out.println("Majority response "+finalValue);

        // Read and repair
        for (String node : getResponseValueMap.keySet()) {

            resValue = getResponseValueMap.get(node);
            if (!resValue.equals(finalValue)) {
                System.out.println("Read Repair for node "+node + " " + getResponseValueMap.get(node));
                DistributedCacheService cache = new DistributedCacheService(node);
                cache.put(key, finalValue);
            }
        }

        return finalValue;
    }
}
