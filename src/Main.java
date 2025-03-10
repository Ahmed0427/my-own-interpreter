import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import jline.console.completer.StringsCompleter;
 

public class Main {

    static boolean isThereError = false;
    static boolean isThereRuntimeError = false;
    
    static Scope scope = new Scope();
    final static Scope global = scope;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            runREPL();
        }
        else if (args.length == 1) {
            run(Files.readString(Paths.get(args[0])));
            if (isThereRuntimeError) System.exit(1);
            if (isThereError) System.exit(1);
        }
        else {
            System.exit(1);
        }
    }

    private static void runREPL() throws IOException {
        ConsoleReader reader = new ConsoleReader();

        reader.setHistory(new FileHistory(new File("history.txt")));

        reader.setExpandEvents(false); 

        String[] keywords = {
            "false", "true",
            "if", "else", "while", "for",
            "or", "and",
            "class", "super", "this",
            "fun",
            "none", 
            "print",
            "return",
            "let", "const"
        };

        reader.addCompleter(new StringsCompleter(keywords));

        while (true) {
            try {
                String line = reader.readLine("> ");

                if (line == null || line.equals("exit")) break;

                Tokenizer tokenizer = new Tokenizer(line);
                List<Token> tokens = tokenizer.scanTokens();

                if (tokens.size() == 1) continue;
                Token lastToken = tokens.get(tokens.size() - 2);
                
                int leftBraceCnt = 0;
                int rightBraceCnt = 0;
                for (Token token : tokens) {
                    if (token.type == TokenType.RIGHT_BRACE) rightBraceCnt++; 
                    if (token.type == TokenType.LEFT_BRACE) leftBraceCnt++; 
                }

                while (leftBraceCnt > rightBraceCnt ||
                    (lastToken.type != TokenType.RIGHT_BRACE &&
                    lastToken.type != TokenType.SEMICOLON)) {

                    line = reader.readLine(">>> ");

                    if (line == null || line.equals("exit")) return;

                    Tokenizer contTokenizer = new Tokenizer(line);
                    List<Token> contTokens = contTokenizer.scanTokens();

                    for (Token token : contTokens) {
                        if (token.type == TokenType.RIGHT_BRACE) rightBraceCnt++; 
                        if (token.type == TokenType.LEFT_BRACE) leftBraceCnt++; 
                    }

                    tokens.remove(tokens.size() - 1);

                    tokens.addAll(contTokens);

                    lastToken = tokens.get(tokens.size() - 2);

                }

                parseAndEval(tokens);

                isThereError = false;
                isThereRuntimeError = false;
            }
            catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
        }
    }

    private static void run(String source) {
        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.scanTokens();

        parseAndEval(tokens);
    }

    private static void parseAndEval(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        List<StmtNode> statements = parser.parse();

        if (isThereError) return;

        try {
            addNativeFunctions();
            for(StmtNode stmt : statements) {
                stmt.evaluate();
            }
        }
        catch (EvaluationException ex) {
            reportEvaluationError(ex);
        }
        catch (ReturnVal ex) {
            String msg = "Uncorrect use of return statement";
            reportEvaluationError(new EvaluationException(ex.keyword, msg));
        }
    }

    private static void addNativeFunctions() {
        global.namesMap.put("clock", new MyCallable() {
            @Override
            public int parametersCount() {
                return 0;
            }

            @Override
            public Object call(List<Object> arguments) {
                return (double)System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    static void reportError(int line, String message) {
        printError(line, message, "");
    }

    static void reportError(Token token, String message) {
        String where = "";
        if (token.type == TokenType.EOF) where = "at end";
        else where = "at '" + token.lexeme + "'";
        printError(token.line, message, where);
    }

    private static void printError(int line, String message, String where) {
        String errMsg = "[line " + line + "] Error " + where + ": " + message;
        System.err.println(errMsg);
        isThereError = true;
    }

    static void reportEvaluationError(EvaluationException ex) {
        System.err.println("[line " + ex.token.line + "] " + ex.getMessage());
        isThereRuntimeError = true;
    } 
}
