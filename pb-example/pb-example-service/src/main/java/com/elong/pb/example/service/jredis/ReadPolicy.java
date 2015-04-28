package com.elong.pb.example.service.jredis;

import java.lang.reflect.Method;

/**
 * project：pb-monitor-common
 * Date: 15/4/24
 * Time: 15:08
 * file: ReadPolicy.java
 *
 * @author dewei.tian@corp.elong.com
 */
public interface ReadPolicy {
    Object invokeRead(Method method, Object[] args);
}
