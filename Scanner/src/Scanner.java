
import java.util.HashMap;


public class Scanner {
    public static void main(String[] args) throws Exception {
        var lookup = new HashMap<Character, Integer>();
        char[] alpha = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                        'A', 'B',  'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        int i = 0;
        for (char c: alpha) {
            lookup.put(alpha[i], i);
            i++;
        }
        lookup.put('+', 52);
        lookup.put('-', 52);
        lookup.put('/', 52);
        lookup.put('%', 52);
        lookup.put('*', 52);
        lookup.put('+', 52);
        int state = 0;
        String strInput = "x = x + 1;";
        char[] input = strInput.toCharArray();
        for (int val : input) {
            System.out.print(val + "");
        }
        int[] acc = {};
        int[][] fsm = {
            /* State 0 */  {},
            {}
        };
        char c = 'x';
        while (input != null && valid(c)) {
    
            }
         }
    
        private static boolean isAl(char c) {
            return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z');
        }
    
        private static boolean isNum(char c) {
             return (c >= '0' && c <= '9');
        }
    
        private static boolean isSymbol(char c) {
            return (c == ';') || (c == '{') || (c == '}') || (c == '(') || (c == ')') || (c == '+') || 
            (c == '-') || (c == '*')|| (c == '/') || (c == '>') || (c == '<') || (c == '=') || (c == ',') || (c == '%') || (c == '!');
        }
        private static boolean valid(char c) {
            return isAl(c) || isNum(c) || isSymbol(c) || isSpace(c) || isNewline(c);
        }
    
        private static boolean isNewline(char c) {
            return c == '\n';
        }
    
        private static boolean isSpace(char c) {
            return c == ' ';
        }
        


}

