package dev.somemathew.compiler.program;

import dev.somemathew.compiler.drivers.MainDriver;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    private static final String PROGRAM_NAME = "ToyCompiler";

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor(PROGRAM_NAME).build() //
                .description(PROGRAM_NAME + " is a driver for the Comp442 Compiler.") //
                .epilog("Use \"" + PROGRAM_NAME + " [command] --help\" for more information about a command."); //

        parser.addArgument("file") //
                .help("File to parse.") //
                .action(Arguments.store()) //
                .metavar("SOURCE_FILE") //
                .dest("inFileName");

        parser.addArgument("--output", "-o") //
                .help("Output files prefix.") //
                .action(Arguments.store()) //
                .metavar("OUTPUT_FILE") //
                .dest("outputFileName");

        try {
            LOGGER.setLevel(Level.WARNING);
            Namespace namespace = parser.parseArgs(args);
            String inputFileName = namespace.getString("inFileName");
            String outputFileName = namespace.getString("outputFileName");

            MainDriver main = new MainDriver(inputFileName, outputFileName);
            main.execute(inputFileName, outputFileName);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
