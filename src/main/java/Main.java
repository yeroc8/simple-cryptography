import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import java.io.IOException;


public class Main {

    private static Options options = new Options();

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
        }
        options.addOption("h", "help", false, "display this message");
        options.addOption("d", "decode", false, "decode input instead of encode");
        options.addOption(Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("file")
                .desc("input file [default: stdin]")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("file")
                .desc("output file [default: stdout]")
                .build());
        try {
            run(args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp();
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void run(String[] args) throws ParseException, IOException, IllegalArgumentException {
        CommandLine cli = new DefaultParser().parse(options, args);
        byte[] input;
        if (cli.hasOption('i')) {
            input = Files.readAllBytes(Paths.get(cli.getOptionValue('i')));
        } else {
            input = IOUtils.toByteArray(System.in);
        }
        UniversalDetector detector = new UniversalDetector();
        detector.handleData(input);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        Cipher cipher = new Cipher(new String(input, encoding), args[0]);
        cipher.transpose(cli.hasOption('d'));
        if (cli.hasOption('o')) {
            Files.write(Paths.get(cli.getOptionValue('o')), cipher.getText().getBytes(), CREATE);
        } else {
            System.out.println(cipher.getText());
        }
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar simple-cryptography.jar <key> [options]", options);
    }
}
