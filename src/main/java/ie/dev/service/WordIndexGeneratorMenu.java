package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;


public final class WordIndexGeneratorMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordIndexGeneratorMenu.class);
    private boolean keepRunning = true;
    private final Scanner scanner;

    private Path libraryPath = Path.of("library");

    private Set<String> ignoreWords;
    private Map<String, List<String>> definitions;
    private final GenerateIndex indexGenerator;


    public WordIndexGeneratorMenu(Scanner scanner, GenerateIndex indexGenerator){
        this.scanner = scanner;
        this.indexGenerator = indexGenerator;
    }

    public void print(){
        System.out.println(
        """
        ____________________________________
        Index Generator
        ____________________________________
        1. Parse ebook, print to console
        ____________________________________
        2. Parse ebook, print index to file
        ____________________________________
        3. Search definition by word
        ____________________________________
        4. Exit
        
        Enter Option [1-4]
                """);
    }

    public void start() throws Exception{
        initialize();
        runMenuLoop();
    }

    public void initialize() throws Exception {
        indexGenerator.loadIgnoreWords();
        indexGenerator.loadDefinitions();
        print();
    }

    public void runMenuLoop() throws IOException {
        while (keepRunning){
            int option = scanner.nextInt();
            handleOption(option);
        }
    }

    public Map<String, Word> generateIndex(Path bookPath) throws IOException {
        return indexGenerator.generateIndex(bookPath);
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
        Path bookPath = getBookPath();
        printIndexToConsole(generateIndex(bookPath));
    }

    public void saveIndex() throws IOException{
        LOGGER.info("Saving index");
        Path bookPath = getBookPath();
        getBookIndexForSave(bookPath);
    }

    public void getBookIndexForSave(Path bookPath) throws IOException {
        Map<String, Word> bookIndex = generateIndex(bookPath);
        saveIndexToFile(bookIndex, bookPath);
    }

    public void searchIndex() throws IOException{
        LOGGER.info("searching index");
        Path bookPath = getBookPath();
        String word = getWord();
        getBookIndexForSearch(bookPath, word);
    }

    public void getBookIndexForSearch(Path bookPath, String word) throws IOException {
        Map<String, Word> bookIndex = generateIndex(bookPath);
        searchIndexForWord(bookIndex, word);
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

    public Path getBookPath() throws IOException{
        LOGGER.info("Getting book path...");
        System.out.println(listBooks(libraryPath));
        System.out.println("Enter path and name of book to create index for: ");
        String bookPath = scanner.next();
        return Path.of(bookPath);
    }
    public void printIndexToConsole(Map<String, Word> bookIndex){
        LOGGER.info("Printing index to console...");
        for (Map.Entry<String, Word> entry : bookIndex.entrySet()) {
            System.out.println(entry.getKey() + "\n" + entry.getValue());
        }
    }

    public void saveIndexToFile(Map<String, Word> bookIndex, Path bookPath) throws IOException {
        LOGGER.info("Saving index to file...");
        //Path inputPath = Path.of(bookPath);
        Path outputFile = Path.of("output",bookPath.getFileName().toString().replace(".txt", "_index.txt"));

        try(BufferedWriter writer = Files.newBufferedWriter(outputFile))
            {
            writer.write("Word\t\t\tDetails");
            writer.newLine();

            for(Map.Entry<String, Word> entry : bookIndex.entrySet()){
                writer.write("""
                                     %-15s Definition%n\t\t\t\t%s%n\t\t\t\tPage Nos %s%n"""
                                     .formatted(
                                             entry.getKey(),
                                             entry.getValue().definitions(),
                                             entry.getValue().pageNumbers()));
            }

        }
        LOGGER.info("\nIndex saved to file: {}", outputFile.toAbsolutePath());
    }

    public void searchIndexForWord(Map<String, Word> bookIndex, String word){
        LOGGER.info("Searching index for word...");
        Word entry = bookIndex.get(word);
        if (entry == null){
            LOGGER.info("Word not found in index: {}", word);
            System.out.println("Your word is not in the index");
            return;
        }
        System.out.println("Word\t\t\t\tDetails");
        System.out.printf(
                "%-15s Definition%n\t\t\t\t%s%n\t\t\t\tPage Nos %s%n",
                word,
                entry.definitions(),
                entry.pageNumbers()
        );

    }

    public String getWord(){
        LOGGER.info("Get word to search...");
        System.out.println("Enter the word to search for: ");
        String word = scanner.next();
        return word;
    }
}
