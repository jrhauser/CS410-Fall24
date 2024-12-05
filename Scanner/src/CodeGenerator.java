import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class CodeGenerator {
    static int[] labelTable = new int[1000];
    static int pc = 2000;
    static int mem = 4000;
    static int reg = 1;
    public static void main(String args[]) { 

    } 
    public static void codeGen(ArrayList<List<Object>> atoms) {
        
    }

    public static void buildLabels(ArrayList<List<Object>> atoms) {
        int i = 0;
        for (var atom : atoms) {
            if (atom.get(1).equals("LBL")) {
                labelTable[i++] = pc;
            } else if (atom.get(1).equals("MOV")) {
                labelTable[500 + i++] = mem;
                mem += 4;
                pc += 2;
            } else if (atom.get(i).equals("JMP")){
                pc += 2;
            } else {
                pc += 3;
            }
        }
    }

    private String addAtom() {
        BitSet bits = new BitSet();
        bits.set(3); // 0001
        for (int i = 4; i > 0; i--) {
           if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable[pc + 500] >> i & 1) == 1)
                 bits.set(i + 31);
         }
        return bits.toString();
    }

    private String subAtom() {
        BitSet bits = new BitSet();
        bits.set(2); // 0010
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable[pc + 500] >> i & 1) == 1)
                bits.set(i + 31);
        }
        return bits.toString();
    }

    private String mulAtom() {
        BitSet bits = new BitSet();
        bits.set(2); // 0011
        bits.set(3); // 0011
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable[pc + 500] >> i & 1) == 1)
                bits.set(i + 31);
        }
        return bits.toString();
    }

    private String divAtom() {
        BitSet bits = new BitSet();
        bits.set(1); //0100
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable[pc + 500] >> i & 1) == 1)
                bits.set(i + 31);
        }
        return bits.toString();
    }


}
