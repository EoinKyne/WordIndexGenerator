package ie.dev;

import ie.dev.service.GenerateIndex;
import ie.dev.service.WordIndexGeneratorMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Scanner;


public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        LOGGER.info("Startup...");
        try {
            WordIndexGeneratorMenu menu = new WordIndexGeneratorMenu(new Scanner(System.in), new GenerateIndex());
            menu.start();
        }catch (IOException e){
            LOGGER.error("IOException occurred: {}", e);
        }catch (Exception e){
            LOGGER.error("Exception occurred: {}", e);
        }
    }
}
