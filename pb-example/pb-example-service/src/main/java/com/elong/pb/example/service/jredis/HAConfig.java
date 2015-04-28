package com.elong.pb.example.service.jredis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.List;

/**
 * project：pb-monitor-common
 * Date: 15/4/24
 * Time: 15:04
 * file: HAConfig.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class HAConfig {
    private GenericObjectPoolConfig poolConfig;
    private List<ServerInfo> servers;

    public HAConfig(final GenericObjectPoolConfig poolConfig, List<ServerInfo> servers) {
        this.poolConfig = poolConfig;
        this.servers = servers;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public List<ServerInfo> getServers() {
        return servers;
    }

    public void setServers(List<ServerInfo> servers) {
        this.servers = servers;
    }
}