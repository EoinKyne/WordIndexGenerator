package ie.dev;

import ie.dev.service.WordIndexGeneratorMenu;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static Set<String> ignoreWords;
    private static Map<String, List<String>> definitions;

    public static void main(String[] args) {
        LOGGER.info("Startup...");
        try {
            ignoreWords = IgnoreWordsLoader.load();
            Reader reader = new BufferedReader(new InputStreamReader(
                    DefinitionsLoader.class.getResourceAsStream("/templates/dictionary.csv")
            ));
            definitions = DefinitionsLoader.load(reader);
        }catch (IOException e){
            LOGGER.error(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu();
        menu.start();
        //System.out.println(ignoreWords);
        //System.out.println(definitions.get("a"));
    }
}
