import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Current Problems:
//  Expression only takes two operands, should be recursive
//  Need to call Expression() within Declaration() instead of on its own
//  Prepare for ; to signify a new statement so more than one or two atoms get made ever

public class ParserTest {
    //token list declaration
    private static ArrayList<String> tokens = new ArrayList<>();
    private static String currentToken;
    //atom list declaration
    private static List<List<Object>> atoms = new ArrayList<List<Object>>();
    private static ArrayList<String> queryTokens = new ArrayList<>();
    private static final Pattern textToken = Pattern.compile("Accepted: Class: (\\w+|[+\\-/%*{};=<>!().]+)( [^ V\\n" + //
                "]+)?(?: Value: (\\w+.* *\\w*))*");
    
    
    
  public static void main(String args[]) {
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
    //declare your test tokens
    tokens.add("Identifier: a");
    tokens.add("+");
    tokens.add("Integer Literal: 5");
    System.out.println(tokens);
    
   Program();
    
    System.out.println("valid input");
    System.out.println(atoms);

    tokens.clear();
    atoms.clear();

    tokens = queryTokens;
    System.out.println(tokens);

    Program();
    
    System.out.println("valid input");
    System.out.println(atoms);
  }
  
  //The necessary simplifiers
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
  
  //atom generation function
  static void atom(String instruction, Object operand1, Object operand2, Object result){
      List<Object> newAtom = new ArrayList<Object>();
      newAtom.add(instruction);
      newAtom.add(operand1);
      newAtom.add(operand2);
      newAtom.add(result);
      atoms.add(newAtom);
  }
  static void ifAtom(String instruction, Object left, Object right, Object cmp, Object dest){
    List<Object> newAtom = new ArrayList<Object>();
    newAtom.add(instruction);
    newAtom.add(left);
    newAtom.add(right);
    newAtom.add(",");
    newAtom.add(cmp);
    newAtom.add(dest);
    atoms.add(newAtom);
}

static void jump(Object dest){
    List<Object> newAtom = new ArrayList<Object>();
    newAtom.add("JMP");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(dest);
    atoms.add(newAtom);
}

static void label(Object dest){
    List<Object> newAtom = new ArrayList<Object>();
    newAtom.add("LBL");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(",");
    newAtom.add(dest);
    atoms.add(newAtom);
}
  
 
  
  //begin the nonterminals
  static void Program() {
      currentToken = getNextToken();
      Object nextToken = peekNextToken();
      //Im ignoring for, if, and while cases for now. Uncommenting Declaration() works how youd expect
      
    if(accept("int")||accept("float")) {
        Declaration();
    }
    else if(currentToken.startsWith("Identifier: ")&&nextToken.equals("=")){
        Assignment();
    }
    else if (accept("if")) {
        If();
    }
    else if (accept("while")) {
        While();
    }
    else if (accept("for")) {
        For();
    }
    else{
      Expression();
    }
  }
  static void Declaration() {
      Type();
      Assignment();
  }
  
  static void For() {
    String instruction = "TST";
    expect("(");
    Declaration();
    List<Object> condition = Condition();
    Object dest = "FIX";
    ifAtom(instruction, condition.get(0), condition.get(2), condition.get(1), dest);
    expect(";");
    Expression();
    expect(")");
    //expect("{");
    Program();
    expect("}");

  }
  static void While() {
    String instruction = "TST";
    accept("while");
    expect("(");
    List<Object> condition = Condition();
    Object dest = "FIX";
    ifAtom(instruction, condition.get(0), condition.get(2), condition.get(1), dest);
    expect(")");
    //expect("{");
    Program();
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
      //expect("{");
      Program();
      jump("END");
      label("ELSE");
      expect("}");
      Else();
      label("END");

  }

  static void Else() {
        expect("else");
        //expect("{");
        Program();
        expect("}");
    }

  static Object Comparison() {
    if (accept("==")) {
        return 7-1;
    } else if (accept("!=")) {
        return 7-6;
    } else if (accept("<")) {
        return 7-2;
    } else if (accept("<=")) {
        return 7-4;
    } else if (accept(">")) {
        return 7-3;
    } else if (accept(">=")) {
        return 7-5;
    }
    return null;
}


  static void Assignment() {
      String instruction = "MOV";
      Object dest = Name();
      expect("=");
      Object source = Value();
      expect(";");
      atom(instruction, source, ",", dest);
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
      if(currentToken.startsWith("Identifier:")){
        Object token = currentToken.substring(12);
        accept(currentToken);
        return token;
      }
      reject();
      return null;

  }
  static Object Value() {
      if(currentToken.startsWith("Integer Literal: ")){
        Object token = currentToken.substring(17);
        accept(currentToken);
          return token;
      }
      else if (currentToken.startsWith("Float Literal: ")){
        Object token = currentToken.substring(15);
        accept(currentToken);
        return token;

      }
      else {

      }
      reject();
      return null;
  }
  
  //ive separated expression into a separate set of functions, need to connect it with declaration somehow
  static Object Expression() {
      Object temp = "temp";
      Object operand1 = Term();
      String instruction = Operator();
      Object operand2 = Term();
      atom(instruction, operand1, operand2, temp);
      return "temp";
  }
  
  static Object Term() {
      Object tempValue;
      if(currentToken.startsWith("Identifier: ")){
          tempValue = currentToken.substring(12, currentToken.length());
          accept(currentToken);
          return tempValue;
      }else if(currentToken.startsWith("Integer Literal: ")){
          tempValue = currentToken.substring(17);
          accept(currentToken);
          return tempValue;
      }
      else if (currentToken.startsWith("Float Literal: ")){
        tempValue = currentToken.substring(15);
          accept(currentToken);
          return tempValue;
      }
      
      reject();
      return "";
  }
  static String Operator(){
      
      if (accept("+")) {
            return "ADD";
        } else if (accept("-")) {
            return "SUB";
        } else if (accept("*")) {
            return "MUL";
        } else if (accept("/")) {
            return "DIV";
        }
        //++ and -- cant be here, might drop them entirely
        reject();
        return "";
  }
  
}