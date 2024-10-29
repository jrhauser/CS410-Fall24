
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Phase2Parser {
    private static String currentToken;
    private static ArrayList<String> queryTokens = new ArrayList<>();
    private static ArrayList<String> tokens = new ArrayList<>();
    private static final Pattern textToken = Pattern.compile("Accepted: Class: (\\w+|[+\\-/%*{};=<>!().]+)( [^ V\\n" + //
                "]+)?(?: Value: (\\w+.* *\\w*))*");
    private static final Pattern numDeclaration = Pattern.compile("(Integer|Float) Literal: ([0-9]+.*[0-9]*)");
    private static final Pattern identifierDeclaration = Pattern.compile("Identifier: ((\\w)+)");
    public static void main(String[] args) {
        ArrayList<String> query = new ArrayList<>();
        query = ProjectOne.partOne();
        for(int i = 0; i < query.size(); i++){
            Matcher textMatcher = textToken.matcher(query.get(i));
            if (!textMatcher.matches()) {
                System.out.println("Error: incorrect syntax for scanner output");
                break;
            }
            String ourClass = textMatcher.group(1).strip();
            if(textMatcher.group(3)!=null){
                String ourValue = textMatcher.group(3).strip();
                if(ourClass.equals("Identifier")){
                queryTokens.add(ourClass+": " + ourValue);
                }
                else{
                    queryTokens.add(ourClass+" Literal: " + ourValue);
                }
            }
            else {
                queryTokens.add(ourClass);
            }

        }
        System.out.println(queryTokens);
        tokens.add("int");
        tokens.add("Identifier: x");
        tokens.add("=");
        tokens.add("Integer: 0");
        tokens.add(";");
        Program();
        System.out.println("valid input");
        if(queryTokens.get(0).equals("int")||queryTokens.get(0).equals("float")){
            System.out.println("declaration");
            Matcher numDecMatcher = numDeclaration.matcher(queryTokens.get(3));
            if(!numDecMatcher.matches()){
                System.out.println("query");
            }
            
            Matcher identMatcher = identifierDeclaration.matcher(queryTokens.get(1));
            if(!identMatcher.matches()){
                System.out.println("query2");
            }
            System.out.println("(MOV, "+ numDecMatcher.group(2)+", , " + identMatcher.group(1)+ ")");
        }


    }
    static boolean accept(String s) {
        var temp = s.equals(currentToken);
        if (temp) {
            currentToken = getNextToken();
        }
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
        currentToken = getNextToken();
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
        expect(";");
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
        if (currentToken == null) {
            return;
        } 
        if (currentToken.equals("EOI")) {
            return;
        }
        else if (acceptName(currentToken)) {
            return;
        }
        reject();
    }
        
    static void Value() {
        if(currentToken == "true" || currentToken == "false"){
            Boolean();
        } else if(acceptInteger(currentToken)){
            Number();
        }
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
        if(currentToken == "true" || currentToken == "false"){
            accept(currentToken);
        }
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
