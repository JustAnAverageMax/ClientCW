package Model;

import java.util.Random;

public class RandomStringGenerator {
    public static String generateString(int length, String alphabet){
        Random rnd = new Random();
        StringBuilder result = new StringBuilder();
        for(int i = 0; i<length; i++){
            result.append(alphabet.charAt(rnd.nextInt(length)));
        }
        return result.toString();
    }
}
