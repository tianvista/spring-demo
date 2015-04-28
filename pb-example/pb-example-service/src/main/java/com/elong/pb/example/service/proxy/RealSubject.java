package com.elong.pb.example.service.proxy;

/**
 * project：pb-example
 * Date: 15/4/27
 * Time: 14:51
 * file: RealSubject.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class RealSubject implements Subject {
    public void operation1() {
        System.out.println("Realer do operation1");
    }

    public void operation2(String arg) {
        System.out.println("Realer do operation2 with " + arg);
    }

}
