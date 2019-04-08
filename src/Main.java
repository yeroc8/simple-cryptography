import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            main(new String[] {"-h"});
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void run(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addOption("h", "help", false, "display this message");
        options.addOption(Option.builder("k").longOpt("key").hasArg().desc("cipher key").required().build());
        options.addOption("d", "decode", false, "decode input instead of encode");
        options.addOption("i", "input", true, "input file [default: stdin]");
        options.addOption("o", "output", true, "output file [default: stdout]");
        CommandLine cli = new DefaultParser().parse(options, args);
        if (cli.hasOption('h')) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar simple-cryptography.jar", options);
            return;
        }
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
        Cipher cipher = new Cipher(new String(input, encoding), cli.getOptionValue('k'));
        cipher.transpose(cli.hasOption('d'));
        if (cli.hasOption('o')) {
            Files.write(Paths.get(cli.getOptionValue('o')), cipher.getText().getBytes(), CREATE);
        } else {
            System.out.println(cipher.getText());
        }
    }
}
