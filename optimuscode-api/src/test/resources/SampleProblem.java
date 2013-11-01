/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/13/13
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleProblem {
    public static String reverseString(String inputStr){
        int length = inputStr.length();
        String reversedStr = "";
        for(int i = length - 1; i >= 0; i--){
            reversedStr += inputStr.charAt(i);
        }
        return reversedStr;
    }
}
