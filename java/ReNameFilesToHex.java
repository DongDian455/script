import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by HeJiaJun on 2019/7/31.
 * Email:455hejiajun@gmail.com
 * des:修改目录下的文件名为16进制
 */
public class ReNameFilesToHex {
    public static void main(String[] args) {
        while (true){
            Scanner sc=new Scanner(System.in);
            System.out.println("输入1为转码，0为解码：");
            boolean isEncode=sc.nextInt()==1;
            System.out.println("输入转码路径：");
            String path=sc.next();
            reName(path,isEncode);
        }
    }


    public static void reName(String path,boolean isEncode){
        File file=new File(path);

        if(!file.exists()){
            System.out.println("路径出错!!!");
            return;
        }

        File[] files=file.listFiles();
        if(files==null||files.length==0){
            System.out.println("路径文件为空!!!");
            return;
        }

        for (File subFile : files) {
            if(subFile.isDirectory()){
                reName(subFile.getAbsolutePath(),isEncode);
            }
            String oldName=subFile.getName();
            String newName=null;
            if(isEncode){
                //转码
                if(!isHexStrValid(oldName)){
                    newName=str2HexStr(subFile.getName());
                }else {
                    System.out.println("文件已经转码!!! filename="+oldName);
                    continue;
                }
            }else {
                //解码
                if(isHexStrValid(oldName)){
                    newName=hexStr2Str(subFile.getName());
                }else {
                    System.out.println("文件已经解码!!! filename="+oldName);
                    continue;
                }
            }
            if (newName != null && !newName.equals("")) {
              boolean renameSuccess=subFile.renameTo(new File(subFile.getParent() + "/" + newName));
              if(!renameSuccess){
                  System.out.println("重命名出错!!! filename="+oldName);
              }else {
                  System.out.println("重命名成功!!! filnename="+newName);
              }
            }
        }
    }



    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static boolean isHexStrValid(String str) {
        String pattern = "^[0-9A-F]+$";
        return Pattern.compile(pattern).matcher(str).matches();
    }
}
