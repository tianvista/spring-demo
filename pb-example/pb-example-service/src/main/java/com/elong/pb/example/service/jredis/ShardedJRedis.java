package com.elong.pb.example.service.jredis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * project：pb-monitor-common
 * Date: 15/4/24
 * Time: 15:15
 * file: ShardedJRedis.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class ShardedJRedis {
    private Sharded<JedisCommands, JRedisShardInfo> shard;
    private final int NODE_SIZES_PER_SHARD = 2;

    public ShardedJRedis(final GenericObjectPoolConfig poolConfig,
                         List<JedisShardInfo> infos, Hashing algo, Pattern keyTagPattern) {
        if (infos.size() % NODE_SIZES_PER_SHARD != 0) {
            throw new RuntimeException("node size for shard must be the time of " + NODE_SIZES_PER_SHARD);
        }

        List<JRedisShardInfo> shardInfos = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < infos.size(); i++) {
            if (i % NODE_SIZES_PER_SHARD == NODE_SIZES_PER_SHARD - 1) {
                shardInfos.add(new JRedisShardInfo(poolConfig, infos.subList(start, i+1)));
                start += NODE_SIZES_PER_SHARD;
            }
        }

        this.shard = new Sharded<>(shardInfos, algo, keyTagPattern);
    }

    public Object invoke(Method method, Object[] args) {
        Object result;
        JedisCommands commands = shard.getShard((String)args[0]);
        try {
            result = method.invoke(commands, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result = null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result = null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

}