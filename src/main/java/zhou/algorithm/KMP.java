package zhou.algorithm;

/**
 * Created by yibingzhou on 2016/10/19.
 * kmp算法
 * 关键在于求前缀函数
 * 下次匹配offset=offeset+(q-l)
 * offset:当前匹配目标串的位置，
 * q:模式串与目标串匹配成功的个数
 * l:匹配成功的模式子串中，满足既是真后缀(不等于自身的后缀)，又是最长真前缀的字符串的长度。
 * 数组来表示前缀函数是比较恰当的，以数组的下标表示已经匹配的字符数 q，以下标对应的数据存储 l
 */
public class KMP {
    static class Position{
        int sourcePos;
        int targetPos;
        int len;

        public Position(int sourcePos, int targetPos, int len) {
            this.sourcePos = sourcePos;
            this.targetPos = targetPos;
            this.len = len;
        }

        public int getSourcePos() {
            return sourcePos;
        }

        public int getTargetPos() {
            return targetPos;
        }

        public int getLen() {
            return len;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "sourcePos=" + sourcePos +
                    ", targetPos=" + targetPos +
                    ", len=" + len +
                    '}';
        }
    }

    public static Position getLCS(char[] source, char[] target)
    {
        int i,j;
        int sLength,tLength;
        sLength = source.length;
        tLength = target.length;
        int max = 0;
        int maxIndex = -1;
        int maxIndex2 = -1;
        int[] c = new int[sLength];

        for (i = 0; i < sLength ; i++)
        {
            for (j = 0; j <= tLength -1; j++)
            {
                if (source[i] == target[j])
                {
                    if ( ( i == 0) || (j == 0) ||source[i-1]!=target[j-1])
                        c[i] = 1;
                    else
                        c[i] = c[i - 1] + 1;
                    if (c[i] > max){
                        max = c[i];
                        maxIndex = i;
                        maxIndex2 = j;
                    }
                }
            }
        }
        for(int k = 0;k<c.length;k++)
            System.out.print(c[k]+" ");
        System.out.println();
        System.out.println(maxIndex);
        System.out.println(maxIndex2);
        return new Position(maxIndex==-1?-1:maxIndex-max+1,maxIndex2==-1?-1:maxIndex2-max+1,max);
    }

    int[] cal_next(char[] pattern){
        int len = pattern.length;
        int next[] = new int[len];
        int index = 0;
        next[0]=-1;
        for(int i =1;i<len;i++){
            index = next[i-1];
            while (index>=0&&pattern[i]!=pattern[index+1]){
                index = next[index];
            }
            if(pattern[i]==pattern[index+1]){
                next[i]=index+1;
            }else{
                next[i]=-1;
            }
        }
        return next;
    }

    int index( char[] str, char[] ptr)
    {
        int p_index = 0;
        int t_index = 0;
        int[] next = cal_next(ptr);
        while (p_index<ptr.length&&t_index<str.length){
            if(str[t_index]==ptr[p_index]){
                ++t_index;
                ++p_index;
            }else if(p_index==0){
                ++t_index;
            }else{
                p_index = next[p_index]+1;
            }
        }
        return (p_index==ptr.length)?(t_index-ptr.length):-1;
    }

    public static void main(String[] args){
        KMP kmp = new KMP();
        String pattern = "abcdabcbabcdcd";
        int [] next=kmp.cal_next(pattern.toCharArray());
        for(int i=0;i<next.length;i++){
            System.out.println(pattern.substring(0,i+1)+"\t"+next[i]);
        }
        int inx = kmp.index("由于时间关系，没能将上述KMP算法的实现细节一一讲清，以后有时间补上".toCharArray(),"时间".toCharArray());
        System.out.println(inx);
    }
}
