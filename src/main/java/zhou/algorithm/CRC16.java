package zhou.algorithm;

/**
 * CRC 校验字节的生成步骤如下:

 装一个 16 位寄存器，所有数位均为 1。
 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算。运算结果放入这个 16 位寄存器。
 把这个 16 寄存器向右移一位。
 若向右(标记位)移出的数位是 1，则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算；若向右移出的数位是 0，则返回步骤(3)。
 重复步骤(3)和步骤(4)，直至移出 8 位。
 取被校验串的下一个字节
 重复步骤(3) ~ 步骤(6)，直至被校验串的所有字节均与 16 位寄存器进行“异或”运算，并移位8 次。
 这个 16 位寄存器的内容即 2 字节 CRC 错误校验码
 */
public class CRC16 {

    private static String getCrc(byte[] data){
        int high,flag;

        int wcrc = 0xffff;//16位寄存器，所有数位均为1
        for(int i=0;i<data.length;i++){
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc=high^data[i];
            for(int j=0;j<8;j++){
                flag=wcrc&0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc=wcrc>>1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if(flag==1)
                    wcrc^=0xa001;
            }
        }
        return Integer.toHexString(wcrc);
    }
    public static void main(String[] args){
        String text = "zfdshga";
        System.out.println(getCrc(text.getBytes()));
    }
}
