package com.elong.pb.example.service.pool;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * project：pb-example
 * Date: 15/4/28
 * Time: 14:31
 * file: GenericObjectPoolDemo.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class GenericObjectPoolDemo {

    public static void main(String[] args) {
        Object obj = null;
        PoolableObjectFactory factory = new PoolableObjectFactorySample();
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.lifo = false;
        config.maxActive = 2;

        ObjectPool pool = new GenericObjectPool(factory, config);
        try {
            for(long i = 0; i < 10 ; i++) {
                System.out.println("== " + i + " ==");
                obj = pool.borrowObject();
                System.out.println(obj);
                pool.returnObject(obj);
            }
            obj = null;//明确地设为null，作为对象已归还的标志
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (obj != null) {//避免将一个对象归还两次
                    pool.returnObject(obj);
                }
                pool.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
