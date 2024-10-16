

public class Phase2Parser {
    private static String inputString;
    public static void main(String[] args) {

    }
    static boolean accept(String s) {
        return s.equals(inputString);
    }

    static void reject() {

    }

    static boolean expect(String s) {
        if (accept(s)) {
            return true;
        }   
        throw new IllegalStateException("Expected a different token");
    }

    static void Program() {
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
        expect("=");
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
        }
        expect("float");
    }

    static void Name() {

    }
    static void Value() {
        Number();
        Boolean();
    } 
    static void Number() {

    } 
    static void Integer() {

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
