package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateIndexTest {

    private IgnoreWordsLoader ignoreWordsLoader = new IgnoreWordsLoader();
    private DefinitionsLoader definitionsloader = new DefinitionsLoader();
    private Set<String> ignoreWords = Set.of("the", "and");

    Map<String, List<String>> definitions = Map.of(
            "java", List.of("A programming language"),
            "book", List.of("A written work")
    );

    @TempDir
    Path tempDir;

    GenerateIndex generateIndex = new GenerateIndex(ignoreWords, definitions);

    @Test
    void shouldGenerateIndexForSingleWord() throws IOException {
        Files.createFile(tempDir.resolve("book1.txt"));
        Files.writeString(tempDir.resolve("book1.txt"), "Hello java. The book is on and around the table.");
        Map<String, Word> result = generateIndex.generateIndex(tempDir.resolve("book1.txt"));
        assertTrue(result.containsKey("hello"));
        assertTrue(result.containsKey("java"));
        assertFalse(result.containsKey("the"));
        assertFalse(result.containsKey("and"));
        assertTrue(result.size() == 7);
    }

    @Test
    void shouldThrowExceptionWhenPathIsInvalid() throws IOException {
        Path invalidPath = Path.of("some-invalid-path");
        assertThrows(IOException.class, () -> generateIndex.generateIndex(invalidPath));
    }

    @Test
    void shouldTokenizeText(){
        String text = "Hello, world! This is a test.";
        String[] tokens = generateIndex.tokenizeLine(text);
        assertTrue(tokens.length == 6);
        assertTrue(tokens[0].equals("hello"));
        assertTrue(tokens[tokens.length-1].equals("test"));
    }

    @Test
    void shouldCreateWordEntry(){
        String word = "hello";
        Word entry = generateIndex.createWordEntry(word);
        assertTrue(entry.word().equals("hello"));
        assertFalse(entry.definitions().isEmpty());
    }

    @Test
    void shouldCreateWordEntryWithDefaultString(){
        String word = "testnonexistentstring";
        Word entry = generateIndex.createWordEntry(word);
        assertTrue(entry.word().equals("testnonexistentstring"));
        assertTrue(entry.definitions().contains("No definition found"));
    }

    @Test
    void shouldAddPageCountToExistingWordEntry(){
        String word = "hello";
        Word entry = generateIndex.createWordEntry(word);
        assertTrue(entry.word().equals("hello"));
        assertFalse(entry.definitions().isEmpty());
        entry = generateIndex.createWordEntry(word);
        assertTrue(entry.word().equals("hello"));
    }

    @Test
    void shouldNotAddPageCountToExistingWordEntry(){
        String word = "hello";
        String differentWord = "world";
        Word entry = generateIndex.createWordEntry(word);
        assertTrue(entry.word().equals("hello"));
        assertFalse(entry.definitions().isEmpty());
        Word differentEntry = generateIndex.createWordEntry(differentWord);
        assertTrue(differentEntry.word().equals("world"));
    }

    @Test
    void shouldUpdatePageCounterWhenLineCountIsForty(){
        int lineCount = 40;
        int pageCount = 1;
        int updatedPageCount = generateIndex.updatePageCounter(lineCount, pageCount);
        assertTrue(updatedPageCount == 2);
    }

    @Test
    void shouldNotUpdatePageCountWhenCountIsNotAMultipleOfForty(){
        int lineCount = 39;
        int pageCount = 1;
        int returnPageCount = generateIndex.updatePageCounter(lineCount, pageCount);
        assertFalse(returnPageCount == 2);
    }
}
