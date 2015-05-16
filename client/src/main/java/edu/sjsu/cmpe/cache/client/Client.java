package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    public static  List<String> nodes;
    public static Map<Long, String> KVpair = new HashMap<Long, String>();
    public static String value;

//    public static ConsistentHashCall ch = new ConsistentHashCall();

    public static void main(String[] args) throws Exception
    {
        System.out.println("\nStarting Cache Client...\n");

        nodes = new ArrayList<String>();

        nodes.add("http://localhost:3000");
        nodes.add("http://localhost:3001");
        nodes.add("http://localhost:3002");

        CRDTClient crdtClient= new CRDTClient(nodes);

        System.out.println("First HTTP PUT call to store a to key 1");
        crdtClient.put(1, "a");

        try {
            System.out.println("Sleep for 30 seconds");
            Thread.sleep(30000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        CRDTClient crdtClient_1= new CRDTClient(nodes);
        System.out.println("Second HTTP PUT call to update key 1 value to b");
        crdtClient_1.put(1, "b");

        System.out.println("Final HTTP GET call to retrieve key 1 value");
        try {
            Thread.sleep(30000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        CRDTClient crdtClient_2= new CRDTClient(nodes);
        value=crdtClient_2.get(1);
        System.out.println("Updating value to: "+value);

        System.out.println("Existing Cache Client...");

    }
}