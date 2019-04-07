import org.apache.commons.cli.*;
import java.nio.file.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addOption("d", "decode", false, "decode input instead of encode");
        options.addOption("i", "input", true, "input file [default: stdin]");
        options.addOption("o", "output", true, "output file [default: stdout]");

        CommandLine cli = new DefaultParser().parse(options, args);
        byte[] input;
        if (cli.hasOption('i')) {
            input = Files.readAllBytes(Paths.get(cli.getOptionValue('i')));
        } else {
            // stdin
        }
        byte[] output = new byte[0];
        if (cli.hasOption('o')) {
            Files.write(Paths.get(cli.getOptionValue('o')), output, StandardOpenOption.CREATE);
        } else {
            System.out.println(new String(output));
        }
    }
}
