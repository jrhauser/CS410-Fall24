
import java.util.ArrayList;



public class Phase2Parser {
    private static String currentToken;
    private static ArrayList<String> tokens = new ArrayList<String>();
    public static void main(String[] args) {
        tokens.add("int");
        tokens.add("Identifier: x");
        tokens.add("=");
        tokens.add("Integer: 0");
        tokens.add(";");
        System.out.println(tokens);
        Program();
        System.out.println("valid input");
    }
    static boolean accept(String s) {
        var temp = s.equals(currentToken);
        currentToken = getNextToken();
        return temp;
    }

    static boolean acceptName(String s) {
        var temp = (s.startsWith("Identifier") && s.split(" ")[1].matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$"));
        currentToken = getNextToken();
        return temp;
    }

    static boolean acceptInteger(String s) {
        var temp = (s.startsWith("Integer") && s.split(" ")[1].matches("/^\\d+$/"));
        currentToken = getNextToken();
        return temp;
    }
    static String getNextToken() {
        if (!tokens.isEmpty()) {
            return tokens.remove(0);
        }
        return "EOI"; 
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

    static void Program() {
        if (accept("EOI")) {
            return;
        }
        if (accept("for")) {
            expect("(");
            Declaration();
            Condition();
            expect(";");
            Expression();
            expect(")");
            expect("{");
            Program();
            expect("}");

        } else if (accept("while")) {
            expect("(");
            Condition();
            expect(")");
            expect("{");
            Program();
            expect("}");
        } else if (accept("if")) {
            expect("(");
            Condition();
            expect(")");
            expect("{");
            Program();
            expect("}");
            Else();
        } else {
            Declaration();
        }
    }

    static void Statement() {

    }

    static void Else() {
        if (accept("else")) {
            expect("else");
            expect("{");
            Program();
            expect("}");
        }
        if (accept("else")) {
            Else();
        }
    }
    static void Declaration() {
        Type();
        Name();
        accept("=");
        Value();
    } 

    static void Assignment() {
        Name();
        expect("=");
        Value();
        expect(";");
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

    static void Name() {
        if (currentToken.equals("EOI")) {
            return;
        }
        else if (acceptName(currentToken)) {
            return;
        }
        reject();
    }
        
    static void Value() {
        return;
    } 
    static void Number() {
        Integer();
    } 
    static void Integer() {
        if (acceptInteger(currentToken)) {
            return;
        }
        reject();
    }
    static void Boolean() {

    } 
    static void Operator() {
        if (accept("+")) {
            return;
        } else if (accept("-")) {
            return;
        } else if (accept("*")) {
            return;
        } else if (accept("/")) {
            return;
        } else if (accept("++")) {
            return;
        }
        expect("--");
    }
    static void Comparison() {
        if (accept("==")) {
            return;
        } else if (accept("!=")) {
            return;
        } else if (accept("<")) {
            return;
        } else if (accept("<=")) {
            return;
        } else if (accept(">")) {
            return;
        }
        expect(">=");
    }

    static void Condition() {
        Name();
        Comparison();
        Value();
    }

    private static void Expression() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
