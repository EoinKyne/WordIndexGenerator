package ie.dev.integration;

import ie.dev.model.Word;
import ie.dev.service.WordIndexGeneratorMenu;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTests {

    private static Set<String> ignoreWords;
    private static Map<String, List<String>> definitions;
    WordIndexGeneratorMenu wordIndexGeneratorMenu = new WordIndexGeneratorMenu(new Scanner(System.in));
    Map<String, Word> bookIndex = Map.of(
            "java", new Word("java", List.of("A programming language"), Set.of(1, 3)),
            "python", new Word("python", List.of("Another programming language"), Set.of(2,4))
    );
    @TempDir
    Path tempDir;

    @Test
    void loadIgnoreWords() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        IgnoreWordsLoader.class.getResourceAsStream("/templates/IgnoreWords.txt")
                )
        )){
            ignoreWords = IgnoreWordsLoader.load();
            assertFalse(ignoreWords.isEmpty());
        }
    }

    @Test
    void loadsRealDictionaryCSV() throws Exception {
        try(Reader reader = new BufferedReader(
                new InputStreamReader(
                        DefinitionsLoader.class.getResourceAsStream("/templates/dictionary.csv")
                )
        )){
            definitions = DefinitionsLoader.load(reader);
            assertFalse(definitions.isEmpty());
        }
    }

    @Test
    void shouldPrintMapToConsole() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        wordIndexGeneratorMenu.printIndexToConsole(bookIndex);
        assertTrue(output.toString().contains("java"));
        assertFalse(output.toString().contains("bash"));
    }

    @Test
    void shouldSaveBookIndexToFile() throws IOException {
        Path input = tempDir.resolve("book_index.txt");

        Map<String, Word> index = Map.of(
                "JavaScript",
                new Word("definition", List.of("A programming language", "A core technology of the Web"), Set.of(1, 2, 3))
        );
        wordIndexGeneratorMenu.saveIndexToFile(index, input);
        Path expected = Path.of("output", input.getFileName().toString().replace(".txt", "_index.txt"));

        assertTrue(Files.exists(expected));

        String content = Files.readString(expected);
        assertTrue(content.contains("programming language"));
        assertTrue(content.contains("core technology of the Web"));
    }
}
