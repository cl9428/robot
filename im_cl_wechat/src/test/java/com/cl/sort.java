package com.cl;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.UUID;

public class sort {

        public static void main(String[] args) {
            sort s = new sort();
            System.out.println(s.maxEvents(new int[][]{{1, 2}, {2, 3}, {3, 4}}));
            int[][] a = new int[][]{{1, 5}, {4, 6}, {3, 4}};
            Arrays.sort(a, ( first,  second) ->first[0]- second[0]);
            for (int j[] : a) {
                for (int i : j) {
                    System.out.print(i + "\t");
                }
            }
            System.out.println(getUUID());
        }
        public int maxEvents(int[][] events) {
            // 按照开始日期升序排列。
            Arrays.sort(events, (first, second) -> first[0] - second[0]);
            // queue 为小顶推。
            PriorityQueue<Integer> queue = new PriorityQueue<>();
            int count = 0;
            int day = events[0][0];
            int res = 0;
            while (count < events.length || !queue.isEmpty()) {
                // 这一天开始开的会议。进行补充
                while (count < events.length && events[count][0] == day) {
                    queue.add(events[count][1]);
                    count++;
                }
                // 从中选出 最近结束的会议。
                while (!queue.isEmpty()) {
                    int remove = queue.remove();// 弹出。
                    if (remove >= day) { // 过滤了一下那些早已结束的会议。
                        res++;
                        break;
                    }
                }
                day++;
            }
            return res;
        }


    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }


}
