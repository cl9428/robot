package com.cl;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class solution {
    public static void main(String[] args) {
       int a[][] =new int[][]{
               {0 ,3, 2,3 ,0, 3},{3,0,3}
               ,{2 ,1, 0}};
        Scanner sn = new Scanner(System.in);
        int n = sn.nextInt();
        int m = sn.nextInt();
       for(int i=0;i<n;i++){
           for(int j=0;j<n;j++){
               if(i==j) {
                   a[i][j]=0;
               }
               if((i+j+1)==m){
                   System.out.println(a[i][j]);
               }
          }
       }
    }
}
