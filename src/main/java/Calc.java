/**
 * @author threedr3am
 */
public class Calc {
  static {
    try {
      Runtime.getRuntime().exec("/System/Applications/Calculator.app/Contents/MacOS/Calculator");
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
