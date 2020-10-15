package me.silloy.lintcode;

/**
 * 描述
 * 给你一个数组和两个索引，交换下标为这两个索引的数字
 *
 * 您在真实的面试中是否遇到过这个题？
 * 样例
 * 给出 [1,2,3,4] index1 = 2, index2 = 3. 交换之后变成 [1,2,4,3]
 */
public class P484_SwapNode {
    /**
     * @param array: An integer array
     * @param index1: the first index
     * @param index2: the second index
     * @return: nothing
     */
    public void swapIntegers(int[] array, int index1, int index2) {
        int a = array[index1];
        int b = array[index2];
        array[index1] = b;
        array[index2] = a;
    }
}
