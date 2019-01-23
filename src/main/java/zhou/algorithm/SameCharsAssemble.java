package zhou.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SameCharsAssemble {

    public boolean isSameWithSort(String s1,String s2){
        if(!preCheck(s1,s2)) return false;
        byte[] b1 = s1.getBytes();
        byte[] b2 = s2.getBytes();
        Arrays.sort(b1);
        Arrays.sort(b2);
        return Arrays.equals(b1,b2);
    }

    public boolean isSameWithMap(String s1,String s2){
        if(!preCheck(s1,s2)) return false;
        Map<Character,Integer> char_counts = new HashMap<>();
        for(int i=0;i<s1.length();i++){
            char c = s1.charAt(i);
            Integer count = char_counts.get(c);
            if(null==count)
                char_counts.put(c,1);
            else char_counts.put(c,count+1);
        }
        for(int i=0;i<s2.length();i++){
            char c = s2.charAt(i);
            Integer count = char_counts.get(c);
            if(null==count)
                return false;//快速失败
            if(count-1==0)
                char_counts.remove(c);
            else
                char_counts.put(c,count-1);
        }
        return char_counts.isEmpty();
    }

    public boolean isSameWithAscii(String s1,String s2) {
        if (!preCheck(s1, s2)) return false;
        byte[] b1 = s1.getBytes();
        byte[] b2 = s2.getBytes();
        byte[] bCount = new byte[256];
        for(int i=0;i<256;i++){
            bCount[i] = 0;
        }
        for(int i=0;i<b1.length;i++)
            bCount[b1[i]-'0']++;
        for(int i=0;i<b2.length;i++)
            bCount[b2[i]-'0']--;
        for(int i=0;i<256;i++){
            if(bCount[i]!=0)
                return false;
        }
        return true;
    }

    private boolean preCheck(String s1,String s2){
        if(null==s1&&s2==null) return true;
        if(null==s1&&null!=s2) return false;
        if(null!=s1&&null==s2) return false;
        if(s1.equals(s2)) return true;
        if(s1.length()!=s2.length()) return false;
        return true;
    }

    public static void main(String[] args){
        SameCharsAssemble sameCharsAssemble = new SameCharsAssemble();
        String s1="acdef",s2="adcfe";
        System.out.println(sameCharsAssemble.isSameWithSort(s1,s2));
        System.out.println(sameCharsAssemble.isSameWithMap(s1,s2));
        System.out.println(sameCharsAssemble.isSameWithAscii(s1,s2));
    }
}
