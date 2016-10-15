package moonvishnya.xyz.luna.registration_validator;


import java.util.Arrays;
import java.util.Collection;

public class Validator {
    private static final Collection<Character> specificChars = Arrays.asList(',', ':', ';', '!', '#', '$', '%', '^', '&',
            '*', '-', '+', '/', '|', ']', '[', '{', '}', '=', '_', '<', '>', '?', ' ');
    public static boolean isValid(String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (specificChars.contains(sentence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
