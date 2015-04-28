package com.elong.pb.example.service.commons.util;

import com.elong.pb.example.domain.Employee;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * project：pb-example
 * Date: 15/4/24
 * Time: 17:12
 * file: PropertyUtilsDemo.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class PropertyUtilsDemo {

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Employee employee = new Employee("a", 1111D, "b", "c");

        PropertyUtils.setSimpleProperty(employee, "name", "ccc");

        String name = (String)PropertyUtils.getSimpleProperty(employee, "name");

        System.err.println(name);

    }
}
