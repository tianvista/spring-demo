package com.elong.pb.example.service.test;

import com.elong.pb.example.service.jredis.ShardedJRedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * project：pb-example
 * Date: 15/4/10
 * Time: 15:21
 * file: ShardedJRedisTest.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class ShardedJRedisTest {

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxIdle(5*1000);
        config.setMaxWaitMillis(1*1000);

        String host = "127.0.0.1";
        int port1 = 6379;
        int port2 = 6380;
        int port3 = 6381;
        int port4 = 6382;

        List<JedisShardInfo> infos = new ArrayList<>();
        infos.add(new JedisShardInfo(host, port1));
        infos.add(new JedisShardInfo(host, port2));
        infos.add(new JedisShardInfo(host, port3));
        infos.add(new JedisShardInfo(host, port4));
        ShardedJRedisPool pool = new ShardedJRedisPool(config, infos);

        String key = "aa";
        String value = "aa";
        pool.getResource().set(key, value);
        System.out.println(pool.getResource().get(key));

        System.err.println("end");
    }
}
