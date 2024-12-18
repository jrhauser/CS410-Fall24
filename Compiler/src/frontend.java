public class frontend {
    public static void main(String[] args) {
        String input_file = args[0];
        String output_file = args[1];
        boolean opFlag = Boolean.valueOf(args[2]);
        ParserPart2.parser(input_file, output_file, opFlag);
}
}
