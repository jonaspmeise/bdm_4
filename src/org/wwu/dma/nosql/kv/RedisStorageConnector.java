package org.wwu.dma.nosql.kv;
import redis.clients.jedis.Jedis;

public class RedisStorageConnector {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");

        jedis.set("key", "oan value");

        jedis.rpush("liste", "value");

        System.out.println(jedis.rpop("liste"));
        System.out.println(jedis.rpop("liste"));
        System.out.println(jedis.get("key"));
    }
}
