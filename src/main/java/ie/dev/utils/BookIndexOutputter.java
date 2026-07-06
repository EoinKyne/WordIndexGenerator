package ie.dev.utils;

import ie.dev.model.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class BookIndexOutputter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookIndexOutputter.class);
    public void printToConsole(Map<String, Word> bookIndex){

        System.out.printf("%-20s%s%n", "Word", "Details");
        System.out.println("-------------------------------------------------------------");

        for (Map.Entry<String, Word> entry : bookIndex.entrySet()) {
            Word word = entry.getValue();

            // Print the word only once
            System.out.printf("%-20sDefinition%n", word.word());

            for (String def : word.definitions()) {

                // Split each definition on embedded newlines
                String[] lines = def.split("\\R");

                for (int i = 0; i < lines.length; i++) {
                    if (i == 0) {
                        System.out.printf("%20s%s%n", "", lines[i]);
                    } else {
                        System.out.printf("%20s%s%n", "", lines[i]);
                    }
                }
            }
            System.out.printf("%20sPage Nos: %s%n", "", word.pageNumbers());
            System.out.println();
        }
    }

    public void saveToFile(Map<String, Word> bookIndex, Path bookPath) throws IOException {
        LOGGER.info("Saving index to file...");
        Path outputFile = Path.of("output",bookPath.getFileName().toString().replace(".txt", "_index.txt"));

        try(BufferedWriter writer = Files.newBufferedWriter(outputFile))
        {
            writer.write("""
                                 %s %23s""".formatted("Word", "Details"));
            writer.newLine();

            for(Map.Entry<String, Word> entry : bookIndex.entrySet()){
                Word word = entry.getValue();
                writer.write("""
                                     %-20s Definition:%n"""
                                     .formatted(
                                             word.word()));

                for (String def : word.definitions()){
                    // Split each definition on embedded newlines
                    String[] lines = def.split("\\R");

                    for (int i = 0; i < lines.length; i++) {
                        if (i == 0) {
                            writer.write("""
                                            %20s %s%n""".formatted("",lines[i]));
                        } else {
                            writer.write("""
                                                 %20s %s%n""".formatted("",lines[i]));
                        }
                    }
                }
                writer.write("""
                                     %20s Page Nos: %s%n%n""".formatted("", word.pageNumbers()));
            }

        }
        LOGGER.info("\nIndex saved to file: {}", outputFile.toAbsolutePath());
    }
}
