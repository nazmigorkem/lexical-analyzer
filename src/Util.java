package src;

public class Util {
    public static boolean contains(char c, char[] arr) {
        for (char d : arr) {
            if (c == d)
                return true;
        }
        return false;
    }
}
