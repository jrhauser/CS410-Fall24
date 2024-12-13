import java.util.ArrayList;
import java.util.List;

public class backend {
    private static List<List<Object>> atoms = new ArrayList<List<Object>>();
    public static void main(String[] args) {
        String input_file = args[0];
        String output_file = args[1];
        CodeGenerator.codeGen(atoms, input_file, output_file);
}
}
