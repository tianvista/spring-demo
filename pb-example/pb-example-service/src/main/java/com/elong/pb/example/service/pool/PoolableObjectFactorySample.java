package com.elong.pb.example.service.pool;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * project：pb-example
 * Date: 15/4/28
 * Time: 14:34
 * file: PoolableObjectFactorySample.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class PoolableObjectFactorySample implements PoolableObjectFactory {

    private static int counter = 0;

    @Override
    public Object makeObject() throws Exception {
        Object obj = String.valueOf(counter++);
        System.err.println("Making Object " + obj);
        return obj;
    }

    @Override
    public void destroyObject(Object obj) throws Exception {
        System.err.println("Destroying Object " + obj);

    }

    @Override
    public boolean validateObject(Object obj) {
/* 以1/2的概率将对象判定为失效 */
        boolean result = (Math.random() > 0.5);
        System.err.println("Validating Object "
                + obj + " : " + result);
        return result;    }

    @Override
    public void activateObject(Object obj) throws Exception {
        System.err.println("Activating Object " + obj);

    }

    @Override
    public void passivateObject(Object obj) throws Exception {
        System.err.println("Passivating Object " + obj);

    }
}
