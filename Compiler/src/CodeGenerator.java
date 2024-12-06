import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;

public class CodeGenerator {
    static HashMap<String, Integer> labelTable = new HashMap<>();
    static int pc = 2000;
    static int mem = 4000;
    static int reg = 1;
    static ArrayList<String> code = new ArrayList<>();

    public static void main(String args[]) {
    }

    public static void codeGen(ArrayList<List<Object>> atoms) {

    }

    public static void buildLabels(ArrayList<List<Object>> atoms) {
        for (var atom : atoms) {
            if (atom.get(1).equals("LBL")) {

            } else if (atom.get(1).equals("MOV")) {
                labelTable.put((String) atom.get(5), mem);
                mem += 4;
                pc += 2;
            } else if (atom.get(1).equals("JMP")) {
                pc += 2;
            } else {
                pc += 3;
            }
        }
    }

    private void tstAtom(ArrayList<Object> atom) {
        BitSet bits = new BitSet();
        bits.set(1, 3); // 0111
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        // setting 20 bits for s2
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get(atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
        bits.clear();
        bits.set(1, 2); // 0110

        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        // setting 20 bits for s2
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get(atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }

    private void addAtom(ArrayList<Object> atom) {
        BitSet bits = new BitSet();
        bits.set(3);
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        // setting 20 bits for s2
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get(atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }

    private void subAtom(ArrayList<Object> atom) {
        BitSet bits = new BitSet();
        bits.set(2); // 0010
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get(atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }

    private void mulAtom(ArrayList<Object> atom) {
        BitSet bits = new BitSet();
        bits.set(2, 3); // 0011
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get(atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }

    private void divAtom(ArrayList<Object> atom) {
        BitSet bits = new BitSet();
        bits.set(1); // 0100
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get((String) atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }

    private void jmpAtom(ArrayList<Object> atom){
        BitSet bits = new BitSet();
        // Gen CMP 0110
        bits.set(1, 2);
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get((String) atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
        bits.clear();

        // Gen JMP 0101
        bits.set(1);
        bits.set(3);
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get((String) atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());

    }

    private void movAtom(ArrayList<Object> atom){
        BitSet bits = new BitSet();
        // Gen LOD 0111
        bits.set(1, 3);
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }

        // Register r
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 11);
        }

        // Memory
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get((String) atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }

        code.add(bits.toString());
        bits.clear();

        // Gen STO 1000
        bits.set(0);
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 7);
        }

        // Register r
        for (int i = 4; i > 0; i--) {
            if ((reg >> i & 1) == 1)
                bits.set(i + 11);
        }

        // Memory
        for (int i = 20; i > 0; i--) {
            if ((labelTable.get((String) atom.get(3)) >> i & 1) == 1)
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }
}
