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
                labelTable.put(atom.get(2).toString(), mem);
                mem += 8;
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
        int opCode = 6; // CMP
        bitList += (opCode << 28); // opcode = 0110
        bitList += (Integer.parseInt(atom.get(5).toString()) << 24); // cmp = comparison number
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3)) << 20);
        code.add("0" + Integer.toBinaryString(bitList));
        jmp(atom.get(6).toString());
    }

    public static void addAtom(List<Object> atom) {
        loadWord(atom.get(2).toString()); // lw first number // lw second number
        Integer bitList = 0;
        int opCode = 1; // ADD
        bitList += (opCode << 28); // opcode = 0001
        bitList += (0 << 24); // cmp = 0
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3).toString()) << 20);
        code.add("000" + Integer.toBinaryString(bitList));
        storeWord(atom.get(4).toString());
    }

    public static void subAtom(List<Object> atom) {
        loadWord(atom.get(2).toString()); // lw first number // lw second number
        Integer bitList = 0;
        int opCode = 2; // SUB
        bitList += (opCode << 28); // opcode = 0010
        bitList += (0 << 24); // cmp = 0
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3).toString()) << 20);
        code.add("00" + Integer.toBinaryString(bitList));
        storeWord(atom.get(4).toString());
    }

    public static void mulAtom(List<Object> atom) {
        loadWord(atom.get(2).toString()); // lw first number // lw second number
        Integer bitList = 0;
        int opCode = 3; // MUL
        bitList += (opCode << 28); // opcode = 0011
        bitList += (0 << 24); // cmp = 0
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3).toString()) << 20);
        code.add("00" + Integer.toBinaryString(bitList));
        storeWord(atom.get(4).toString());
    }

    public static void divAtom(List<Object> atom) {
        loadWord(atom.get(2).toString()); // lw first number // lw second number
        Integer bitList = 0;
        int opCode = 4; // DIV
        bitList += (opCode << 28); // opcode = 0100
        bitList += (0 << 24); // cmp = 0
        bitList += (reg);
        bitList += (labelTable.get(atom.get(3).toString()) << 20);
        code.add("0" + Integer.toBinaryString(bitList));
        storeWord(atom.get(4).toString());
    }

    public static void jmpAtom(List<Object> atom) {
        Integer bitList = 0;
        int opCode = 6; // CMP
        bitList += (opCode << 28); // opcode = 0110
        bitList += (0 << 24); // cmp = comparison number
        bitList += (reg);
        bitList += (0 << 20);
        code.add("0" + Integer.toBinaryString(bitList));
        jmp(atom.get(6).toString());
    }

    public static void movAtom(List<Object> atom) {
        loadWord(atom.get(2).toString());
        storeWord(atom.get(4).toString());
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
        int opCode = 8; // STO
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

    public static void halt() {
        Integer bitList = 0;
        int opCode = 9; // HLT
        bitList += (opCode << 28);
        bitList += (0 << 24); // cmp = 0000
        bitList += (0 << 4); // maybe no needed
        bitList += (0 << 20); // maybe not needed
        code.add(Integer.toBinaryString(bitList));
    }

}
