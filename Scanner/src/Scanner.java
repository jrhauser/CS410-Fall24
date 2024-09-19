
import java.util.HashMap;

public class Scanner {
    public static void main(String[] args) throws Exception {
        var l = new HashMap<Character, Integer>();
        char[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int i = 0; i < 26; i++) {
            l.put(alpha[i], i);
        }
        l.put('0', 26);
        l.put('1', 27);
        l.put('2', 28);
        l.put('3', 29);
        l.put('4', 30);
        l.put('5', 31);
        l.put('6', 32);
        l.put('7', 33);
        l.put('8', 34);
        l.put('9', 35);
        l.put('+', 36);
        l.put('-', 37);
        l.put('/', 38);
        l.put('%', 39);
        l.put('*', 40);
        l.put('{', 41);
        l.put('}', 42);
        l.put(';', 43);
        l.put('=', 44);
        l.put('<', 45);
        l.put('>', 46);
        l.put('!', 47);
        l.put('(', 48);
        l.put(')', 49);
        l.put(' ', 50);
        int[][] fsm = {
            //a  b    c  d   e    f   g  h   i    j   k  l   m   n   o   p  q   r    s    t   u   v   w    x  y   z
            {45, 37, 21, 45, 45, 30, 45, 45, 18, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45,  45, 45, 45, 25, 45, 45, 45, },
            {},
            {},

        };
    }
}
