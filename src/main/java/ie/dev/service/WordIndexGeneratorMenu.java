package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.BookIndexOutputter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;


public final class WordIndexGeneratorMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordIndexGeneratorMenu.class);
    private boolean keepRunning = true;
    private final Scanner scanner;

    private Path libraryPath = Path.of("library");

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
        LOGGER.debug("Starting Word index generator menu...");
        initialize();
        runMenuLoop();
    }

    public void initialize() throws Exception {
        LOGGER.debug("Initializing, loading ignore words and definitions...");
        indexGenerator.loadIgnoreWords();
        indexGenerator.loadDefinitions();
    }

    public void runMenuLoop() throws Exception, IOException, InputMismatchException{
        LOGGER.debug("Running menu loop...");
        while (keepRunning){
            print();
            try{
               int option = scanner.nextInt();
               handleOption(option);
            }catch (InputMismatchException e){
               LOGGER.error("Invalid input, please enter a number between 1 and 4");
               System.out.println("Invalid input, please enter a number between 1 and 4");
               scanner.nextLine(); // clear the invalid input
           }
       }
    }

    public Map<String, Word> generateIndex(Path bookPath) throws IOException {
        LOGGER.debug("Generating index for book...");
        return indexGenerator.generateIndex(bookPath);
    }

    public void handleOption(int option) throws IOException {
        LOGGER.debug("Handling option...");
        switch (option){
            case 1 -> printIndex();
            case 2 -> saveIndex();
            case 3 -> searchIndex();
            case 4 -> keepRunning = false;
            default -> System.out.println("Invalid option");
        }
    }

    public void printIndex() throws IOException {
        LOGGER.debug("Get book and ");
        Path bookPath = getBookPath();
        Map<String, Word> bookIndex = generateIndex(bookPath);
        printIndexToConsole(bookIndex);
    }


    public void saveIndex() throws IOException{
        LOGGER.info("Saving index");
        Path bookPath = getBookPath();
        Map<String, Word> bookIndex = generateIndex(bookPath);
        saveIndexToFile(bookIndex, bookPath);
    }

    public void searchIndex() throws IOException{
        LOGGER.info("searching index");
        Path bookPath = getBookPath();
        String word = getWord();
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
        BookIndexOutputter printToConsole = new BookIndexOutputter();
        printToConsole.printToConsole(bookIndex);
    }

    public void saveIndexToFile(Map<String, Word> bookIndex, Path bookPath) throws IOException {
        LOGGER.info("Saving index to file...");
        BookIndexOutputter bookIndexOutputter = new BookIndexOutputter();
        bookIndexOutputter.saveToFile(bookIndex, bookPath);
    }

    public void searchIndexForWord(Map<String, Word> bookIndex, String word){
        LOGGER.info("Searching index for word...");
        Word entry = bookIndex.get(word);
        if (entry == null){
            LOGGER.info("Word not found in index: {}", word);
            System.out.println("Your word is not in the index");
            return;
        }
        BookIndexOutputter printToConsole = new BookIndexOutputter();
        printToConsole.printToConsole(Map.of(word, entry));
    }

    public String getWord(){
        LOGGER.info("Get word to search...");
        System.out.println("Enter the word to search for: ");
        String word = scanner.next();
        return word;
    }
}
