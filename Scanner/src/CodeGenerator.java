import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.lang.Math;


public class CodeGenerator {
    static int[] labelTable = new int[1000];
    static int pc = 2000;
    static int mem = 4000;
    static int reg = 1;
    public static void main(String args[]) { 
        System.out.println(subAtom());
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
        //Setting op code
        bits.set(3);
        //setting cmp, r1 bits
        for (int i = 4; i > 0; i--) {
           if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        //setting 20 bits for s2
        for (int i = 20; i > 0; i--) {
            if ((labelTable[pc + 500] >> i & 1) == 1)
                 bits.set(i + 31);
         }
        return bits.toString();
    }

    public static String subAtom() {
        //Unfinished. Once memory declaration is figured out there will need to be 2 load instructions to replace a and b declarations, and a store op after
        Integer bitList = 0;
        int opCode = 2;
        //a - b = result
        int b = 12;
        int a = 15;

        //setting opCode
        bitList += (opCode << 28) ;
        //setting cmp
        bitList += (0 << 24) ;
        //setting r1
        bitList += (a << 20);
        //setting memory s2
        bitList += (b << 16);
        //adds leading zeroes depending on op code.
        return "00" + Integer.toBinaryString(bitList);
    }
}
