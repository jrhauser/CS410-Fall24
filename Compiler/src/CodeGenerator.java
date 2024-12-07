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
    static boolean firstPass = true;
    private static List<List<Object>> atoms = new ArrayList<List<Object>>();

    public static void main(String args[]) {
        atoms = ParserPart2.parser();
        codeGen(atoms);
    }

    public static void codeGen(List<List<Object>> atoms) {
        buildLabels(atoms);
        for(var atom: atoms){
            if(atom.get(1).equals("TST")) {
                tstAtom(atom);
            }
            else if (atom.get(1).equals("ADD")) {
                addAtom(atom);

            }
            else if (atom.get(1).equals("SUB")) {
                subAtom(atom);
                
            }
            else if (atom.get(1).equals("MUL")) {
                mulAtom(atom);
                
            }
            else if (atom.get(1).equals("DIV")) {
                divAtom(atom);
                
            }
            else if (atom.get(1).equals("JMP")) {
                jmpAtom(atom);
                
            }
            else if (atom.get(1).equals("MOV")) {
                movAtom(atom);
                
            }
            else {
                //ADD LBL when done
            }
            System.out.println(code);
        }
    }

    public static void buildLabels(List<List<Object>> atoms) {
        if (firstPass){
        for (var atom : atoms) {
            if (atom.get(1).equals("LBL")) {
                labelTable.put((String) atom.get(6),mem);
            } else if (atom.get(1).equals("MOV")) {
                labelTable.put((String) atom.get(5), mem);
                mem += 8;
                pc += 2;
            } else if (atom.get(1).equals("JMP")) {
                labelTable.put((String) atom.get(6), mem);
                mem += 4;
                pc += 2;
            } else if (atom.get(1).equals("TST")) {
                labelTable.put((String) atom.get(6), mem);
                mem += 8;
                pc += 2;
            }
            else if (atom.get(1).equals("CMP")) {
                labelTable.put((String) atom.get(6), mem);
                mem += 8;
                pc += 2;
            }
             else {
                labelTable.put((String) atom.get(5), mem);
                mem += 16;
                pc += 3;
            }
        }
    }
    else {}
    }

    public static void tstAtom(List<Object> atom) {
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

    public static void addAtom(List<Object> atom) {
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

    public static void subAtom(List<Object> atom) {
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

    public static void mulAtom(List<Object> atom) {
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

    public static void divAtom(List<Object> atom) {
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

    public static void jmpAtom(List<Object> atom){
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

    public static void movAtom(List<Object> atom){
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
            if (labelTable.get((String) atom.get(3))!=null&&((labelTable.get((String) atom.get(3)) >> i & 1) == 1))
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
            if (labelTable.get((String) atom.get(3))!=null&&((labelTable.get((String) atom.get(3)) >> i & 1) == 1))
                bits.set(i + 31);
        }
        code.add(bits.toString());
    }
}
