import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;
import java.util.jar.Attributes.Name;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Authored by: Joe Hauser, Ethan Sitler, Kate Merrit
// Reviewed bt: Ahmed Mikky and  Juan Zacarias
public class ParserTest {
    // token list declaration
    private static boolean first = true;
    private static ArrayList<String> tokens = new ArrayList<>();
    private static String currentToken;
    // atom list declaration
    private static List<List<Object>> atoms = new ArrayList<List<Object>>();
    private static ArrayList<String> queryTokens = new ArrayList<>();
    private static final Pattern textToken = Pattern.compile("Accepted: Class: (\\w+|[+\\-/%*{};=<>!().]+)( [^ V\\n" + //
            "]+)?(?: Value: (\\w+.* *\\w*))*");

    public static void main(String args[]) {
        ArrayList<String> query = new ArrayList<>();
        query = ProjectOne.partOne();
        for (int i = 0; i < query.size(); i++) {
            Matcher textMatcher = textToken.matcher(query.get(i));
            if (!textMatcher.matches()) {
                System.out.println("Error: incorrect syntax for scanner output");
                break;
            }
            String ourClass = textMatcher.group(1).strip();
            if (textMatcher.group(3) != null) {
                String ourValue = textMatcher.group(3).strip();
                if (ourClass.equals("Identifier")) {
                    queryTokens.add(ourClass + ": " + ourValue);
                } else {
                    queryTokens.add(ourClass + " Literal: " + ourValue);
                }
            } else {
                queryTokens.add(ourClass);
            }

        }
        tokens = queryTokens;
        System.out.println(queryTokens);
        Program();

        System.out.println("valid input");
        for (int i = 0; i < atoms.size(); i++) {
            System.out.print(atoms.get(i).get(0));
            for (int j = 1; j < atoms.get(i).size() - 2; j++) {
                System.out.print(atoms.get(i).get(j) + ", ");
            }
            System.out.print(atoms.get(i).get(atoms.get(i).size() - 2));
            System.out.print(atoms.get(i).get(atoms.get(i).size() - 1));
            System.out.println("");
        }

        /*
         * tokens.clear();
         * atoms.clear();
         * 
         * tokens = queryTokens;
         * System.out.println(tokens);
         * 
         * Program();
         * 
         * System.out.println("valid input");
         * for (int i = 0; i < atoms.size(); i++) {
         * System.out.print(atoms.get(i).get(0));
         * for (int j = 1; j < atoms.get(i).size() - 2; j++) {
         * System.out.print(atoms.get(i).get(j) + ", ");
         * }
         * System.out.print(atoms.get(i).get(atoms.get(i).size() - 2));
         * System.out.print(atoms.get(i).get(atoms.get(i).size() - 1));
         * System.out.println("");
         * }
         */
    }

    // The necessary simplifiers
    static boolean accept(String s) {
        var temp = s.equals(currentToken);
        if (temp) {
            currentToken = getNextToken();
        }
        return temp;
    }

    static void reject() {
        throw new IllegalStateException("Expected a different token");
    }

    static boolean expect(String s) {
        if (accept(s)) {
            return true;
        }
        throw new IllegalStateException("Expected a different token");
    }

    static String getNextToken() {
        if (!tokens.isEmpty()) {
            return tokens.remove(0);
        }
        return "EOI";
    }

    static String peekNextToken() {
        if (!tokens.isEmpty()) {
            return tokens.get(0);
        }
        return "EOI";
    }

    // atom generation function
    static void atom(String instruction, Object operand1, Object operand2, Object result) {
        List<Object> newAtom = new ArrayList<Object>();
        newAtom.add("(");
        newAtom.add(instruction);
        newAtom.add(operand1);
        newAtom.add(operand2);
        newAtom.add(result);
        newAtom.add(")");
        // System.out.println(newAtom);
        atoms.add(newAtom);
    }

    static void ifAtom(String instruction, Object left, Object right, Object cmp, Object dest) {
        List<Object> newAtom = new ArrayList<Object>();
        newAtom.add("(");
        newAtom.add(instruction);
        newAtom.add(left);
        newAtom.add(right);
        newAtom.add(" ");
        newAtom.add(cmp);
        newAtom.add(dest);
        newAtom.add(")");
        // System.out.println(newAtom);
        atoms.add(newAtom);
    }

    static void jump(Object dest) {
        List<Object> newAtom = new ArrayList<Object>();
        newAtom.add("(");
        newAtom.add("JMP");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(dest);
        newAtom.add(")");
        // System.out.println(newAtom);
        atoms.add(newAtom);
    }

    static void label(Object dest) {
        List<Object> newAtom = new ArrayList<Object>();
        newAtom.add("(");
        newAtom.add("LBL");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(" ");
        newAtom.add(dest);
        newAtom.add(")");
        // System.out.println(newAtom);
        atoms.add(newAtom);
    }

    // begin the nonterminals
    static void Program() {
        if (first) {
            currentToken = getNextToken();
        }
        first = false;
        Object nextToken = peekNextToken();
        // System.out.println(currentToken);
        // Im ignoring for, if, and while cases for now. Uncommenting Declaration()
        // works how youd expect

        if (accept("int") || accept("float")) {
            Declaration();
        } else if (currentToken.startsWith("Identifier: ") && (nextToken.equals("=") || nextToken.equals("+=")
                || nextToken.equals("-="))) {
            Assignment();
        } else if (accept("if")) {
            If();
        } else if (accept("while")) {
            While();
        } else if (accept("for")) {
            For();
        } else {
            Expression();
        }
    }

    static void Declaration() {
        Type();
        Assignment();
    }

    static void For() {
        // System.out.println(tokens);
        String instruction = "TST";
        expect("(");
        Declaration();
        // System.out.println(tokens);
        label("START");
        List<Object> condition = Condition();
        Object dest = "END";
        // System.out.println(tokens);
        ifAtom(instruction, condition.get(0), condition.get(2), condition.get(1), dest);
        // System.out.println(tokens);
        expect(";");
        // System.out.println(tokens);
        Expression();
        // System.out.println(tokens);
        expect(")");
        // System.out.println(tokens);
        accept("{");
        System.out.println(currentToken);
        Program();
        jump("START");
        label("END");
        accept("}");

    }

    static void While() {
        label("START");
        String instruction = "TST";
        expect("(");
        List<Object> condition = Condition();
        Object dest = "END";
        ifAtom(instruction, condition.get(0), condition.get(2), condition.get(1), dest);
        expect(")");
        expect("{");
        Program();
        jump("START");
        label("END");
        System.out.println(currentToken);
        expect(";");
        expect("}");
    }

    static List<Object> Condition() {
        List<Object> results = new ArrayList<Object>();
        Object left = Name();
        Object cmp = Comparison();
        Object right = Value();
        results.add(left);
        results.add(cmp);
        results.add(right);
        return results;

    }

    static void If() {
        String instruction = "TST";
        accept("if");
        expect("(");
        List<Object> condition = Condition();
        Object dest = "ELSE";
        ifAtom(instruction, condition.get(0), condition.get(2), condition.get(1), dest);
        expect(")");
        expect("{");
        Program();
        jump("END");
        label("ELSE");
        System.out.println(currentToken);
        System.out.println(currentToken);
        expect(";");
        expect("}");
        System.out.println(currentToken);
        Else();
        label("END");

    }

    static void Else() {
        if (accept("else")) {
            Program();
        }
    }

    static Object Comparison() {
        if (accept("==")) {
            return 7 - 1;
        } else if (accept("!=")) {
            return 7 - 6;
        } else if (accept("<")) {
            return 7 - 2;
        } else if (accept("<=")) {
            return 7 - 4;
        } else if (accept(">")) {
            return 7 - 3;
        } else if (accept(">=")) {
            return 7 - 5;
        }
        return null;
    }

    static void Assignment() {
        String instruction = "MOV";
        Object dest = Name();
        if (accept("=")) {
            Object source = Expression();
            if (accept(";")) {
                atom(instruction, source, " ", dest);
            } else {
                atom(instruction, source, " ", dest);
            }

        } else if (accept("+=")) {
            Object operand2 = Value();
            atom("ADD", dest, operand2, dest);
        } else if (accept("-=")) {
            Object operand2 = Value();
            atom("SUB", dest, operand2, dest);
        } else {

        }
        // expect(";");

    }

    static void Type() {
        if (accept("int")) {
            return;
        } else if (accept("bool")) {
            return;
        } else {
            accept("float");
        }
    }

    static Object Name() {
        if (currentToken.startsWith("Identifier:")) {
            Object token = currentToken.substring(12);
            accept(currentToken);
            return token;
        }
        reject();
        return null;

    }

    static Object Value() {
        if (currentToken.startsWith("Integer Literal: ")) {
            Object token = currentToken.substring(17);
            accept(currentToken);
            return token;
        } else if (currentToken.startsWith("Float Literal: ")) {
            Object token = currentToken.substring(15);
            accept(currentToken);
            return token;

        } else {

        }
        reject();
        return null;
    }

    // ive separated expression into a separate set of functions, need to connect it
    // with declaration somehow
    static Object Expression() {
        Object temp = "temp";
        // System.out.println(currentToken);
        if (currentToken.startsWith("Integer Literal: ") && peekNextToken().equals(";")) {
            Object token = currentToken.substring(17);
            accept(currentToken);
            return token;
        } else if (currentToken.startsWith("Float Literal: ") && peekNextToken().equals(";")) {
            Object token = currentToken.substring(15);
            accept(currentToken);
            return token;

        } else {
            // System.out.println("here");
            Object operand1 = Term();
            String instruction = Operator();
            Object operand2;
            if (instruction.equals("++")) {
                // System.out.println("here");
                instruction = "ADD";
                operand2 = 1;
                // accept(currentToken);
                temp = operand1;
            } else if (instruction.equals("--")) {
                instruction = "SUB";
                operand2 = 1;
                // accept(currentToken);
                temp = operand1;
            } else {
                operand2 = Term();
            }
            atom(instruction, operand1, operand2, temp);
            while (currentToken.equals("+") || currentToken.equals("-") || currentToken.equals("*")
                    || currentToken.equals("/")) {
                // System.out.println("here");
                operand1 = temp;
                instruction = Operator();
                if (instruction.equals("++")) {
                    // System.out.println("here");
                    instruction = "ADD";
                    operand2 = 1;
                    // accept(currentToken);
                    temp = operand1;
                } else if (instruction.equals("--")) {
                    instruction = "SUB";
                    operand2 = 1;
                    // accept(currentToken);
                    temp = operand1;
                } else {
                    operand2 = Term();
                }
                atom(instruction, operand1, operand2, temp);
            }
            return "temp";
        }
    }

    static Object Term() {
        Object tempValue;
        // System.out.println(currentToken);
        if (currentToken.startsWith("Identifier: ")) {
            tempValue = currentToken.substring(12, currentToken.length());
            accept(currentToken);
            return tempValue;
        } else if (currentToken.startsWith("Integer Literal: ")) {
            tempValue = currentToken.substring(17);
            accept(currentToken);
            return tempValue;
        } else if (currentToken.startsWith("Float Literal: ")) {
            tempValue = currentToken.substring(15);
            accept(currentToken);
            return tempValue;
        }

        reject();
        return "";
    }

    static String Operator() {

        if (accept("+")) {
            return "ADD";
        } else if (accept("-")) {
            return "SUB";
        } else if (accept("*")) {
            return "MUL";
        } else if (accept("/")) {
            return "DIV";
        } else if (accept("=")) {
            return "MOV";
        } else if (accept("+=")) {
            return "ADDI";
        } else if (accept("++")) {
            return "++";
        }
        // ++ and -- cant be here, might drop them entirely
        reject();
        return "";
    }

}