package com.cl;

import java.util.Scanner;
import java.util.UUID;

public class mei {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        String m = sn.nextLine();
        String n = sn.nextLine();
        char [] arry1 = m.toCharArray();
        char[] arry2 = n.toCharArray();
        int count = 0;
        boolean f = true;
        int j=0;
        while(f){
            for(int i=0; i<m.length();i++){
                if(j<(n.length()-1)&&arry2[j]==arry1[i]){
                    count += i;
                    j=j+1;
                }
            }
        }


        System.out.println(count);
        System.out.println(getUUID());
    }

    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

}

//abcdefghijklmnopqrstuvwxyz
