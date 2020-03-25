/**
 * @author threedr3am
 */
public class Calc {
  static {
    try {
      System.out.println("run Calc...");
      Runtime.getRuntime().exec("/System/Applications/Calculator.app/Contents/MacOS/Calculator");
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
