package ie.dev;

import ie.dev.model.Word;
import ie.dev.service.GenerateIndex;
import ie.dev.service.WordIndexGeneratorMenu;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    private static Set<String> ignoreWords;
    private static Map<String, List<String>> definitions;

    public static void main(String[] args) throws Exception {
        LOGGER.info("Startup...");
        Reader reader = new BufferedReader(new InputStreamReader(
                DefinitionsLoader.class.getResourceAsStream("/templates/dictionary.csv")
        ));
        ignoreWords = IgnoreWordsLoader.load();
        definitions = DefinitionsLoader.load(reader);
        GenerateIndex indexGenerator = new GenerateIndex(ignoreWords, definitions);
        try {
            for (Map.Entry<String, Word> entry : indexGenerator.generateIndex(Path.of("library/WarAndPeace-Tolstoy.txt"))
             .entrySet()) {
                System.out.println(entry.getKey() + "\n" + entry.getValue());

            }
        }catch (IOException e){
            LOGGER.error(e.getMessage());
        }
    }
}
