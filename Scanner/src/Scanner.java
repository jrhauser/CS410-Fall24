
import java.util.HashMap;

public class Scanner {
    public static void main(String[] args) throws Exception {
        var l = new HashMap<Character, Integer>();
        char[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z' };
        for (int i = 0; i < 51; i++) {
            l.put(alpha[i], i);
        }
        l.put('+', 52);
        l.put('-', 53);
        l.put('/', 54);
        l.put('%', 55);
        l.put('*', 56);
        l.put('{', 57);
        l.put('}', 58);
        l.put(';', 59);
        l.put('=', 60);
        l.put('<', 61);
        l.put('>', 62);
        l.put('!', 63);
        l.put('(', 64);

        l.put('0', 65);
        l.put('1', 66);
        l.put('2', 67);
        l.put('3', 68);
        l.put('4', 69);
        l.put('5', 70);
        l.put('6', 71);
        l.put('7', 72);
        l.put('8', 73);
        l.put('9', 74);

        l.put(')', 75);
        l.put(' ', 76);
        // max state 47
        String strInput = "xyz = x + 1";
        char[] input = strInput.toCharArray();
        var fsm = new int[48][76];
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 76; j++) {
                fsm[i][j] = 47;
            }
        }
        fsm[0][24] = 2;
        fsm[2][60] = 3;
        fsm[3][24] = 4;
        fsm[4][52] = 5;
        fsm[5][66] = 6;
        fsm[6][59] = 7;

        for (int i = 0; i < input.length; i++) {
            var possibleVar = "";
            if (input[i] == 76)
                continue;
            while (input[i] != 76) {
                if (l.get(input[i]) == 8) {
                    break;
                } else if (l.get(input[i]) == 2) {
                    break;
                } else if (l.get(input[i]) == 5) {
                    break;
                } else {
                    possibleVar = possibleVar + input[i];
                    i++;
                }
            }
            if (l.get(input[i]) == 60)
                System.out.println("equals");

            if (l.get(input[i]) == 52)
                System.out.println("plus");

            if (l.get(input[i]) == 66)
                System.out.println("1: literal");

            if (l.get(input[i]) == 59)
                System.out.println("semicolon");
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
                (c == '-') || (c == '*') || (c == '/') || (c == '>') || (c == '<') || (c == '=') || (c == ',')
                || (c == '%') || (c == '!');
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
