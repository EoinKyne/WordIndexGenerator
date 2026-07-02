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
import java.util.Scanner;
import java.util.Set;

public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        LOGGER.info("Startup...");
        try {
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in));
            menu.start();
        }catch (IOException e){
            LOGGER.error("IOException occurred: {}", e.getMessage());
        }catch (Exception e){
            LOGGER.error("Exception occurred: {}", e.getMessage());
        }
    }
}
