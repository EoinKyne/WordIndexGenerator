package ie.dev.utils;

import ie.dev.model.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookIndexOutputterTest {

    BookIndexOutputter bookIndexOutputter = new BookIndexOutputter();

    @TempDir
    Path tempDir;

    Map<String, Word> bookIndex = Map.of(
            "java", new Word("java", List.of("A programming language"), Set.of(1, 3)),
            "python", new Word("python", List.of("Another programming language." +
                                                         "Widely used in web\n projects"), Set.of(2,4))
    );

    @Test
    void shouldPrintMapToConsole() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        bookIndexOutputter.printToConsole(bookIndex);
        assertTrue(output.toString().contains("java"));
        assertFalse(output.toString().contains("bash"));
    }

    @Test
    void shouldSaveBookIndexToFile() throws IOException {
        Path input = tempDir.resolve("book_index.txt");

        bookIndexOutputter.saveToFile(bookIndex, input);
        Path expected = Path.of("output", input.getFileName().toString().replace(".txt", "_index.txt"));

        assertTrue(Files.exists(expected));

        String content = Files.readString(expected);
        assertTrue(content.contains("programming language"));
        assertTrue(content.contains("Another programming language"));
    }
}
