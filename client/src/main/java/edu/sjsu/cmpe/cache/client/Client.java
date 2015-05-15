package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;

import java.util.*;

public class Client {

    public static  List<CacheServiceInterface> cacheList;
    public static Map<Long, String> KVpair = new HashMap<Long, String>();

//    public static ConsistentHashCall ch = new ConsistentHashCall();

    public static void main(String[] args) throws Exception
    {
        System.out.println("\nStarting Cache Client...\n");

        cacheList = new ArrayList<CacheServiceInterface>();

        cacheList.add(new DistributedCacheService("http://localhost:3000"));
        cacheList.add(new DistributedCacheService("http://localhost:3001"));
        cacheList.add(new DistributedCacheService("http://localhost:3002"));

        KVpair.put(new Long(1), "a");
        KVpair.put(new Long(2), "b");
        KVpair.put(new Long(3), "c");
        KVpair.put(new Long(4), "d");
        KVpair.put(new Long(5), "e");
        KVpair.put(new Long(6), "f");
        KVpair.put(new Long(7), "g");
        KVpair.put(new Long(8), "h");
        KVpair.put(new Long(9), "i");
        KVpair.put(new Long(10), "j");

        consistentHash(cacheList,KVpair);
    }

    public static void consistentHash(List<CacheServiceInterface> cacheList, Map<Long, String> KVpair)
    {
        ConsistentHash<CacheServiceInterface> consistentHash;

        Integer replicationFactor = 10;


        Set set = KVpair.entrySet();
        Iterator iterator = set.iterator();

        consistentHash = new ConsistentHash<CacheServiceInterface>(Hashing.md5(), replicationFactor, cacheList);

        while (iterator.hasNext()) {

            Map.Entry mapentry = (Map.Entry) iterator.next();

            CacheServiceInterface bucket = consistentHash.get(mapentry.getKey());
            bucket.put((Long) mapentry.getKey(), (String) mapentry.getValue());
            System.out.println("put(" + mapentry.getKey() + " => " + mapentry.getValue() + ")");

        }
        for (int key = 1; key <= 10; key++) {
            CacheServiceInterface bucket = consistentHash.get(key);
            String value = bucket.get(key);
            System.out.println("get(" + key + ") => " + value);
        }
        System.out.println("\nValue Distribution: \n" +
                        "Server A => http://localhost:3000/cache/  =>\n" + cacheList.get(0).getAllValues() +
                        "\nServer B => http://localhost:3001/cache/  =>\n" + cacheList.get(1).getAllValues() +
                        "\nServer C => http://localhost:3002/cache/  =>\n" + cacheList.get(2).getAllValues()
        );

    }

}