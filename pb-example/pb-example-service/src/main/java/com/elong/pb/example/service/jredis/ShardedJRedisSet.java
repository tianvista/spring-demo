package com.elong.pb.example.service.jredis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * project：pb-monitor-common
 * Date: 15/4/24
 * Time: 15:17
 * file: ShardedJRedisSet.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class ShardedJRedisSet {
    private List<ShardedJRedisNode> nodes;

    public ShardedJRedisSet(List<ShardedJRedisNode> nodes) {
        this.nodes = nodes;
    }

    public ShardedJRedisSet(final GenericObjectPoolConfig poolConfig,
                            List<JedisShardInfo> shardInfos, Hashing algo, Pattern keyTagPattern) {
        List<ShardedJRedisNode> nodes = new ArrayList<>(2);

        List<JedisShardInfo> infos = shardInfos.subList(0, shardInfos.size()/2);
        nodes.add(new ShardedJRedisNode(infos, new ShardedJedisPool(poolConfig, infos, algo, keyTagPattern)));

        infos = shardInfos.subList(shardInfos.size()/2, shardInfos.size());
        nodes.add(new ShardedJRedisNode(infos, new ShardedJedisPool(poolConfig, infos, algo, keyTagPattern)));
    }

    public void addShardedJRedisNode(ShardedJedisPool pool, List<JedisShardInfo> infos) {
        nodes.add(new ShardedJRedisNode(infos, pool));
    }

    public List<ShardedJRedisNode> getShardedJRedisNodes() {
        return this.nodes;
    }
}
