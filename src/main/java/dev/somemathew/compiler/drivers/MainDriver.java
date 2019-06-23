package dev.somemathew.compiler.drivers;

public class MainDriver {
    private static final String TOKEN_EXTENSION = ".tok.txt";
    private static final String ATOCC_EXTENSION = ".atocc";
    private static final String DERIVATION_EXTENSION = ".drv.txt";
    private static final String ERR_EXTENSION = ".err.txt";
    private static final String PROG_EXTENSION = ".m";
    private static final String DOT_EXTENSION = ".dot";
    private static final String TABLE_EXTENSION = ".table.txt";

    private String tokenFile;
    private String derivFile;
    private String errFile;
    private String progFile;
    private String dotFile;
    private String tableFile;
    private String atoccFile;

    public MainDriver(String inputFileName, String outputFileName) {
        if (outputFileName == null) {
            outputFileName = inputFileName;
        }
        this.tokenFile = outputFileName + TOKEN_EXTENSION;
        this.derivFile = outputFileName + DERIVATION_EXTENSION;
        this.errFile = outputFileName + ERR_EXTENSION;
        this.progFile = outputFileName + PROG_EXTENSION;
        this.dotFile = outputFileName + DOT_EXTENSION;
        this.tableFile = outputFileName + TABLE_EXTENSION;
        this.atoccFile = outputFileName + ATOCC_EXTENSION;
    }

    public void execute(String inFile, String outFile) {
        // TODO method stub
    }
}
