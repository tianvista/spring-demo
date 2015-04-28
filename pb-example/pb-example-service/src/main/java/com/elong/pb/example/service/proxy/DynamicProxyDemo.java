package com.elong.pb.example.service.proxy;

import java.lang.reflect.Proxy;

/**
 * project：pb-example
 * Date: 15/4/27
 * Time: 14:50
 * file: DynamicProxyDemo.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class DynamicProxyDemo {

    public static void consumer(Subject subject) {
        subject.operation1();
        subject.operation2("ZJ");
    }

    public static void main(String[] args) {
        RealSubject real = new RealSubject();
        System.out.println("===Without Proxy===");
        consumer(real);
        System.err.println("===Use Proxy===");
        Subject proxy = (Subject) Proxy.newProxyInstance(Subject.class
                        .getClassLoader(), new Class[]{Subject.class},
                new DynamicProxyHandler(real));
        consumer(proxy);

    }
}
