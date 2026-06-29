package ie.dev.integration;

import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class IntegrationTests {

    private static Set<String> ignoreWords;
    private static Map<String, List<String>> definitions;

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
}
