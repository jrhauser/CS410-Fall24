import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class backend {
    private static List<List<Object>> atoms = new ArrayList<List<Object>>();
    public static void main(String[] args) {
        String input_file = args[0];
        String output_file = args[1];
        boolean opLocal = Boolean.valueOf(args[2]);

        
        try{
            Path file = Paths.get(input_file);
            Stream<String> lineStream;
            lineStream = Files.lines(file, Charset.defaultCharset());
            List<String> lineList = lineStream.toList();
            List<List<Object>> newAtoms = new ArrayList<List<Object>>();
            List<Object> newAtom = new ArrayList<Object>();
            for(int i = 0; i < lineList.size(); i++){
                //System.out.println(lineList.get(i));
                newAtom.add("(");
                newAtom.add(lineList.get(i).substring(1,4));
                String[] line = lineList.get(i).split(",");
                for(int k = 0; k < line.length; k++){
                    //System.out.println(line[k].hashCode() + " " + line[k].length());
                }
                for(int j = 1; j < line.length-1; j++){
                    if(line[j].hashCode()==1024){
                    //System.out.println("added a space");
                    newAtom.add(" ");
                    }
                    else{
                    newAtom.add(line[j].trim());
                    }
                }
                newAtom.add(line[line.length-1].substring(0,line[line.length-1].length()-1).trim());
                newAtom.add(")");
                //System.out.println(newAtom);
                newAtoms.add(List.copyOf(newAtom));
                newAtom.clear();
            }
           // System.out.println(newAtoms.toString());
         atoms.addAll(newAtoms);
         
            CodeGenerator.codeGen(atoms,input_file, output_file, opLocal);
        }
        catch (IOException e) {
        e.printStackTrace();
    }
}
}
