/**
 * @author threedr3am
 */
public class Calc {
  static {
    try {
      Runtime.getRuntime().exec("/Applications/Calculator.app/Contents/MacOS/Calculator");
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

  }
}
