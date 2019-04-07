import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("d", "decode", false, "decode input instead of encode");
        options.addOption("i", "input", true, "input file [default: stdin]");
        options.addOption("o", "output", true, "output file [default: stdout]");
        try {
            CommandLine cli = new DefaultParser().parse(options, args);
            if (cli.hasOption('i')) {
                // input file
            } else {
                // stdin
            }
            if (cli.hasOption('o')) {
                // output file
            } else {
                // stdout
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}
