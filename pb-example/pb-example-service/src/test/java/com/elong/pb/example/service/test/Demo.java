package com.elong.pb.example.service.test;

/**
 * project：pb-example
 * Date: 15/4/24
 * Time: 13:44
 * file: Demo.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class Demo {
    public static void main(String[] args) {

        Demo demo = new Demo();

        System.err.println(demo.factorial(1000));
    }

    public int factorial(int m)
    {
        if (m <= 0) {
            return 0;
        } else {
            if ( m == 1)
                return 1;
            else {
                int sum = 1;
                for (int i = 2; i <= m; i++)
                    sum = sum * i;
                return sum;
            }
        }
    }
}
