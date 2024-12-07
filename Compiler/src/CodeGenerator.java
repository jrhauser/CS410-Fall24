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
        for (var atom : atoms) {
            if (atom.get(1).equals("TST")) {
                tstAtom(atom);
            } else if (atom.get(1).equals("ADD")) {
                addAtom(atom);

            } else if (atom.get(1).equals("SUB")) {
                subAtom(atom);

            } else if (atom.get(1).equals("MUL")) {
                mulAtom(atom);

            } else if (atom.get(1).equals("DIV")) {
                divAtom(atom);

            } else if (atom.get(1).equals("JMP")) {
                jmpAtom(atom);

            } else if (atom.get(1).equals("MOV")) {
                movAtom(atom);

            } else {
                // ADD LBL when done
            }
            System.out.println(code);
        }
    }

    public static void buildLabels(List<List<Object>> atoms) {
        for (var atom : atoms) {
            if (atom.get(1).equals("LBL")) {
                labelTable.put(atom.get(6).toString(), pc);
            } else if (atom.get(1).equals("MOV")) {
                labelTable.put(atom.get(5).toString(), mem);
                mem += 8;
                pc += 2;
            } else if (atom.get(1).equals("JMP")) {
                pc += 2;
            } else if (atom.get(1).equals("TST")) {
                labelTable.put(atom.get(2).toString(), mem += 8);
                labelTable.put(atom.get(3).toString(), mem += 8);
                pc += 3;
            } else {
                labelTable.put(atom.get(2).toString(), mem += 8);
                labelTable.put(atom.get(3).toString(), mem += 8);
                labelTable.put(atom.get(4).toString(), mem += 8);
                pc += 3;
            }
        }
    }

    private static void tstAtom(List<Object> atom) {
        loadWord(atom.get(2).toString());
        Integer bitList = 0;
        int opCode = 6; // LOD
        bitList += (opCode << 28); // opcode = 0111
        bitList += (Integer.parseInt(atom.get(5).toString()) << 24); // cmp = comparison number
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3)) << 20);
        code.add("0" + Integer.toBinaryString(bitList));
        bitList = 0;
        opCode = 5;
        bitList += (opCode << 28); // opcode = 0101
        bitList += (Integer.parseInt(atom.get(5).toString()) << 24); // cmp = 0000
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3)) << 20);
        jmp(atom.get(6).toString());
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

    public static void jmpAtom(List<Object> atom) {
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

    public static void movAtom(List<Object> atom) {
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

    public static void loadWord(String identifier) {
        Integer bitList = 0;
        int opCode = 7; // LOD
        bitList += (opCode << 28); // opcode = 0111
        bitList += (0 << 24); // cmp = 0000
        bitList += (reg);
        bitList += (labelTable.get(identifier) << 20); // r1 = (ADD, ___ , , )
        // memory register
        code.add("0" + Integer.toBinaryString(bitList));
    }

    private static void storeWord(String identifier) {
        Integer bitList = 0;
        int opCode = 8; // LOD
        bitList += (opCode << 28); // opcode = 1000
        bitList += (0 << 24); // cmp = 0000
        bitList += (reg);
        bitList += (labelTable.get(identifier) << 20); // memory
        code.add(Integer.toBinaryString(bitList));
    }

    private static void jmp(String label) {
        Integer bitList = 0;
        int opCode = 5; // JMP
        bitList += (opCode << 28); // opcode = 0101
        bitList += (0 << 24); // cmp = 0000
        bitList += (labelTable.get(label) << 20); // memory
        code.add("0" + Integer.toBinaryString(bitList));
    }

}
