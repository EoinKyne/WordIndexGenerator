package ie.dev.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;


public final class WordIndexGeneratorMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordIndexGeneratorMenu.class);
    private static boolean keepRunning = true;
    private static final Scanner scanner = new Scanner(System.in);

    private static Path libraryPath = Path.of("library");

    public WordIndexGeneratorMenu(){}

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

    public void start(){
        print();
        while (keepRunning){
            int option = scanner.nextInt();
            handleOption(option);

        }
    }

    public void handleOption(int option){
        switch (option){
            case 1 -> printIndex();
            case 2 -> saveIndex();
            case 3 -> searchIndex();
            case 4 -> keepRunning = false;
            default -> System.out.println("Invalid option");
        }
    }

    public void printIndex(){
        LOGGER.info("print index");
        try {
            System.out.println(listBooks(libraryPath));
        }catch (IOException e){
            LOGGER.error(e.getMessage());
        }
    }

    public void saveIndex(){
        LOGGER.info("saving index");
    }

    public void searchIndex(){
        LOGGER.info("searching index");
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
}
