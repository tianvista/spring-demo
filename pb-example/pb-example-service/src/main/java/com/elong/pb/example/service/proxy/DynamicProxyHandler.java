package com.elong.pb.example.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * project：pb-example
 * Date: 15/4/27
 * Time: 14:47
 * file: DynamicProxyHandler.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class DynamicProxyHandler implements InvocationHandler {

    private Object proxied;

    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**** proxy: ****\n" + proxy.getClass()
                + "\nmethod: " + method + "\nargs: " + args);
        if (args != null)
            for (Object arg : args)
                System.out.println("  " + arg);
        return method.invoke(proxied, args);    }
}
