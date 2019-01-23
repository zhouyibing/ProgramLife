package zhou.algorithm;

public class SetTest {

    public static void main(String[] args) {

        String str[] = {"A", "B", "C", "D", "E"};

        int nCnt = str.length;

//              int nBit = (0xFFFFFFFF >>> (32 - nCnt));

        int nBit = 1<<nCnt;

        for (int i = 1; i <= nBit; i++) {
            for (int j = 0; j < nCnt; j++) {
                if ((1<<j & i ) != 0) {
                    System.out.print(str[j]);
                }
            }
            System.out.println("");
        }

    }
}