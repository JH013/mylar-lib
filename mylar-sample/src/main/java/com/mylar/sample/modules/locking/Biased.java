package com.mylar.sample.modules.locking;

import java.util.List;
import java.util.Vector;

/**
 * @author wangz
 * @date 2022/1/9 0009 0:06
 */
public class Biased {

    public static List<Integer> numberList = new Vector<Integer>();

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int count = 0;
        int startnum = 0;
        while (count < 10000000) {
            numberList.add(startnum);
            startnum += 2;
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
