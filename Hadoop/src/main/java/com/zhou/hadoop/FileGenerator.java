package com.zhou.hadoop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by yibingzhou on 2016/10/12.
 */
public class FileGenerator {

    public static void main(String[] args) throws ParseException, IOException {
        String str1 = "19700101";
        String str2 = "30161012";
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(sf.parse(str1));
        end.setTime(sf.parse(str2));
        BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\Users\\yibingzhou\\Desktop\\input.txt"));
        Random random = new Random();
        int days = 0;
        while(start.before(end)){
            bw.write(sf.format(start.getTime())+random.nextInt(40));
            bw.write("\n");
            start.add(Calendar.DAY_OF_MONTH,1);
            days++;
        }
        bw.flush();
        bw.close();
        System.out.println("days:"+days);
    }
}
