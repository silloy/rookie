package me.silloy.shiro;

import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class InterviewTest {

    /**
     * 1.求每个评论的点赞数,按逆序排列(sql题)
     * select comment_id, count(user_id) AS startCount
     * from comment_star group by comment_id order by count(user_id) desc;
     */


    /**
     * 2. 有n个日志文件,每个文件有m行,每一行记录一次日志,格式是[time
     * log], 单个日志文件里面的日志已经按照时间递增排序完成,现在需要将n
     * 个日志文件合并为同一个日志文件, 要求:最后得到的日志文件中的日志按
     * 照时间递增排序, 不能使用库函数或脚本中已经实现好的排序算法和工具,
     * 需要自己实现数据结构和所需要的算法.(编程题)
     *
     * 归并排序
     */
    @Test
    public void Quetion2() {
        List<List<LogFile>> fileList = Lists.newArrayList();
        List<LogFile> finalFile = Lists.newArrayList();
        int[] tmpArr = new int[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            Iterator<LogFile> file = fileList.get(i).iterator();
            // 取第一个

        }
    }

    @Data
    public class LogFile {
        private Date time;
        private String logContent;
    }



    /**
     * 3.查找数组arr(arr[i]>0,i>=0)中第k大的奇数,如果不存在则返回0,
     * 要求:希望尽量不要对整个数组进行排序,计算出时间复杂度并附上说
     * 明,不能使用库函数或者脚本中已经实现好的排序算法和工具,需要自
     * 己实现数据结构和所需要的算法(编程题)
     * example:
     * arr = {1,2,3,4}
     * 第2大奇数为 1
     *
     * 快速排序
     */
    @Test
    public void Quetion3() {
        int[] arr = new int[]{23, 34, 56, 55, 2, 5, 78};
        int k = 2;
        sort(arr, 0, arr.length - 1);
        System.out.println(maxK(arr, k));
    }

    public static void sort(int[] arr, int l, int r) {
        if (l < r) {
            int index = patition(arr, l, r);
            sort(arr, l, index - 1);
            sort(arr, index + 1, r);
        }
    }

    public static int patition(int[] arr, int l, int r) {
        int p = arr[l];
        int i = l;
        int j = r;
        while (i < j) {
            while (arr[j] >= p && i < j) {
                j--;
            }
            while (arr[i] <= p && i < j) {
                i++;
            }
            swap(arr, i, j);
        }
        swap(arr, l, i);
        return i;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    public int maxK(int[] arr, int k) {
        int index = 1;
        for (int value : arr) {
            if (value % 2 != 0) {
                if (index == k) {
                    return value;
                }
                index++;
            }
        }
        return 0;
    }


    /**
     * 4. 设计类似微博的推荐Feed流,要求:尽量让用户看到 新鲜,精彩,不
     * 重复的feed流(设计题)
     */
}
