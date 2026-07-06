package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class WordIndexGeneratorMenuTest {

    WordIndexGeneratorMenu wordIndexGeneratorMenu = new WordIndexGeneratorMenu(new Scanner(System.in), new GenerateIndex());
    @TempDir
    Path tempDir;

    @Test
    void shouldPrintMenu(){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        wordIndexGeneratorMenu.print();
        assertTrue(output.toString().contains("Index Generator"));
        assertTrue(output.toString().contains("print to console"));
        assertTrue(output.toString().contains("print index to file"));
        assertTrue(output.toString().contains("definition by word"));
        assertTrue(output.toString().contains("Exit"));
    }

    @Test
    void shouldGenerateIndexForBook() throws Exception {
        // Create a temporary book file
        Path bookPath = tempDir.resolve("book.txt");
        Files.writeString(bookPath, "This is a test book. This book is for testing.");

        // Load ignore words and definitions
        wordIndexGeneratorMenu.initialize();

        // Generate index
        Map<String, Word> index = wordIndexGeneratorMenu.generateIndex(bookPath);

        // Verify that the index contains expected words
        assertTrue(index.containsKey("test"));
        assertTrue(index.containsKey("book"));
        assertFalse(index.containsKey("is")); // Assuming "is" is in ignore words
    }
    @Test
    void shouldReturnAllBooks() throws IOException {
        Files.createFile(tempDir.resolve("book1.txt"));
        Files.createFile(tempDir.resolve("book2.txt"));

        List<Path> books = wordIndexGeneratorMenu.listBooks(tempDir);
        assertEquals(2, books.size());
        assertTrue(books.contains(tempDir.resolve("book1.txt")));
        assertFalse(books.contains(tempDir.resolve("book3.txt")));
    }

    @Test
    void shouldReturnEmptyListWhenLibraryIsEmpty() throws IOException {
        List<Path> books = wordIndexGeneratorMenu.listBooks(tempDir);
        assertTrue(books.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenInvalidPath() throws IOException{
        Path invalidPath = Path.of("some-invalid-path");

        assertThrows(IOException.class,
                     () -> wordIndexGeneratorMenu.listBooks(invalidPath));
    }

    @Test
    void shouldExitWhenOptionIsFour() throws IOException {
        Scanner scanner = new Scanner("4\n");
        WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(scanner, new GenerateIndex());
        assertDoesNotThrow(menu::runMenuLoop);
    }


    @Test
    void shouldSetKeepRunningToFalseWhenOptionIsFour() throws IOException{
        int option = 4;
        wordIndexGeneratorMenu.handleOption(option);
        assertFalse(wordIndexGeneratorMenu.isRunning());
    }

    @Test
    void shouldThrowExceptionWhenInvalidType() throws Exception {
        InputStream originalIn = System.in;
        try {
            String option = "invalid\n4\n";
            System.setIn(new ByteArrayInputStream(option.getBytes()));
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in), new GenerateIndex());
            assertDoesNotThrow(menu::runMenuLoop);

        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void shouldReturnStringInvalidForAnyOtherKey() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        wordIndexGeneratorMenu.handleOption(99);
        assertTrue(output.toString().contains("Invalid option"));
    }


    @Test
    void shouldReturnTheBookPath() throws IOException {
        InputStream originalIn = System.in;
        try {
            String book = "book1.txt";
            System.setIn(new ByteArrayInputStream(book.getBytes()));
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in), new GenerateIndex());
            Path result = menu.getBookPath();

            assertEquals("book1.txt", result.toString());
        }
        finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void shouldReturnWordToSearch(){
        InputStream originalIn = System.in;

        try {
            String word = "java";
            System.setIn(new ByteArrayInputStream(word.getBytes()));
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in), new GenerateIndex());
            String result = menu.getWord();
            assertEquals("java", result.toString());
        }finally {
            System.setIn(originalIn);
        }
    }
}
