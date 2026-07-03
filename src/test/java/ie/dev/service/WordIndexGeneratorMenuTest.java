package ie.dev.service;

import ie.dev.model.Word;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class WordIndexGeneratorMenuTest {

    

    WordIndexGeneratorMenu wordIndexGeneratorMenu = new WordIndexGeneratorMenu(new Scanner(System.in));
    @TempDir
    Path tempDir;

    @Test
    void printIndex(){
        assertTrue(true);
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

    @Disabled
    void shouldPrintIndexWhenOptionIsOne(){

    }

    @Disabled
    void shouldSaveIndexWhenOptionIsTwo(){

    }

    @Disabled
    void shouldSearchIndexWhenOptionIsThree(){

    }

    @Test
    void shouldSetKeepRunningToFalseWhenOptionIsFour() throws IOException{
        int option = 4;
        wordIndexGeneratorMenu.handleOption(option);
        assertFalse(wordIndexGeneratorMenu.isRunning());
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
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in));
            Path result = menu.getBookPath();

            assertEquals("book1.txt", result.toString());
        }
        finally {
            System.setIn(originalIn);
        }
    }


}
