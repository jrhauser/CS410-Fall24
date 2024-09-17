public class Scanner {
    
    public static void main(String[] args) throws Exception {
        int state;
            
        String strInput = "x = x + 1;";
        char[] input = strInput.toCharArray();
        int[] acc = {};
        int[][] fsm = {
            {},
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

