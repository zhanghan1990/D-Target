package com.components;

/**
 * Created by zhanghan on 17/7/2.
 */

import java.util.ArrayList;
import java.util.List;


public class Combine {

    public List zuhe(int[] a, int m) {
        Combine zuhe = new Combine();
        List list = new ArrayList();
        int n = a.length;

        boolean flag = false;


        int[] tempNum = new int[n];
        for (int i = 0; i < n; i++) {
            if (i < m) {
                tempNum[i] = 1;

            } else {
                tempNum[i] = 0;
            }

        }


        list.add(zuhe.createResult(a, tempNum, m));

        do {
            int pose = 0;
            int sum = 0;
            for (int i = 0; i < (n - 1); i++) {
                if (tempNum[i] == 1 && tempNum[i + 1] == 0) {
                    tempNum[i] = 0;
                    tempNum[i + 1] = 1;
                    pose = i;
                    break;
                }
            }
            list.add(zuhe.createResult(a, tempNum, m));


            for (int i = 0; i < pose; i++) {
                if (tempNum[i] == 1)
                    sum++;
            }

            for (int i = 0; i < pose; i++) {
                if (i < sum)
                    tempNum[i] = 1;
                else
                    tempNum[i] = 0;
            }

            flag = false;
            for (int i = n - m; i < n; i++) {

                if (tempNum[i] == 0)
                    flag = true;

            }
        } while (flag);

        return list;
    }


    public int[] createResult(int[] a, int[] temp, int m) {
        int[] result = new int[m];

        int j = 0;
        for (int i = 0; i < a.length; i++) {

            if (temp[i] == 1) {
                result[j] = a[i];
                j++;

            }
        }

        return result;
    }

    public void print1(List list) {

        for (int i = 0; i < list.size(); i++) {
            System.out.println();
            int[] temp = (int[]) list.get(i);
            for (int j = 0; j < temp.length; j++) {
                System.out.print(temp[j] + " ");
            }
        }
    }


//    public static void main(String[] args) {
//        int[] a = { 1, 2, 3, 4, 6 }; // 整数数组
//        int m = 4; // 待取出组合的个数
//        Combine zuhe = new Combine();
//        List list = zuhe.zuhe(a, m);
//        zuhe.print1(list);
//
//    }
}
