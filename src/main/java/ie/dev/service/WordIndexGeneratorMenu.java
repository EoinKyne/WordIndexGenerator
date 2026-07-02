package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;


public final class WordIndexGeneratorMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordIndexGeneratorMenu.class);
    private static boolean keepRunning = true;
    private final Scanner scanner;

    private static Path libraryPath = Path.of("library");

    private static Set<String> ignoreWords;
    private static Map<String, List<String>> definitions;

    public WordIndexGeneratorMenu(Scanner scanner){
        this.scanner = scanner;
    }

    public static void print(){
        System.out.println(
        """
        ____________________________________
        Index Generator
        ____________________________________
        1. Parse ebook, print to console
        ____________________________________
        2. Parse ebook, print index to file
        ____________________________________
        3. Search page number by word
        ____________________________________
        4. Exit
        
        Enter Option [1-4]
                """);
    }

    public void start() throws Exception{
        loadIgnoreWords();
        loadDefinitions();

        print();
        while (keepRunning){
            int option = scanner.nextInt();
            handleOption(option);

        }
    }

    public void loadIgnoreWords() throws IOException {
        ignoreWords = IgnoreWordsLoader.load();
    }

    public void loadDefinitions() throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(
                DefinitionsLoader.class.getResourceAsStream("/templates/dictionary.csv")
        ));
        definitions = DefinitionsLoader.load(reader);
    }

    public Map<String, Word> generateIndex(String bookPath) throws IOException {
        GenerateIndex indexGenerator = new GenerateIndex(ignoreWords, definitions);
        return indexGenerator.generateIndex(Path.of(bookPath));
    }

    public void handleOption(int option) throws IOException{
        switch (option){
            case 1 -> printIndex();
            case 2 -> saveIndex();
            case 3 -> searchIndex();
            case 4 -> keepRunning = false;
            default -> System.out.println("Invalid option");
        }
    }

    public void printIndex() throws IOException {
        LOGGER.info("print index");
        String bookPath = getBookPath();
        printIndexToConsole(generateIndex(bookPath));
    }

    public void saveIndex() throws IOException{
        LOGGER.info("saving index");
        String bookPath = getBookPath();
        generateIndex(bookPath);
    }

    public void searchIndex() throws IOException{
        LOGGER.info("searching index");
        String bookPath = getBookPath();
        generateIndex(bookPath);
    }

    public List<Path> listBooks(Path library) throws IOException {
        LOGGER.info("Get library of books...");
        try (Stream<Path> stream = Files.list(library)){
            return stream.toList();
        }
    }

    public boolean isRunning(){
        return keepRunning;
    }

    public String getBookPath() throws IOException{
        LOGGER.info("Getting book path...");
        System.out.println(listBooks(libraryPath));
        System.out.println("Enter path and name of book to create index for: ");
        String bookPath = scanner.next();
        return bookPath;
    }
    public void printIndexToConsole(Map<String, Word> bookIndex){
        LOGGER.info("Printing index to console...");
        for (Map.Entry<String, Word> entry : bookIndex.entrySet()) {
            System.out.println(entry.getKey() + "\n" + entry.getValue());
        }
    }
}
