package ie.dev.service;

import ie.dev.model.Word;
import ie.dev.utils.DefinitionsLoader;
import ie.dev.utils.IgnoreWordsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class GenerateIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIndex.class);
    private final Set<String> ignoreWords;
    private final Map<String, List<String>> definitions;

    public GenerateIndex(Set<String> ignoreWords, Map<String, List<String>> definitions) {
        LOGGER.info("Starting generate index...");
        this.ignoreWords = Set.copyOf(ignoreWords);
        this.definitions = Map.copyOf(definitions);
    }
    public Map<String, Word> generateIndex(Path book) throws IOException {
        LOGGER.info("Generating index for book...");
        Map<String, Word> bookIndex = new TreeMap<>();

        int lineCount = 0;
        int pageCount = 1;

        try (BufferedReader reader = Files.newBufferedReader(book)){
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                pageCount = updatePageCounter(lineCount, pageCount);

                for (String word : tokenizeLine(line)) {
                    if (ignoreWords.contains(word)) {
                        continue;
                    }
                    Word entry = bookIndex.computeIfAbsent(word, w -> createWordEntry(w));
                    entry.addPageNumber(pageCount);
                }
            }
        }
        return bookIndex;
    }


    public String[] tokenizeLine(String line){
        LOGGER.debug("Tokenizing line: ");
        return line.replaceAll("[^a-zA-Z]", " ")
                .toLowerCase().split("\\s+");
    }


    public Word createWordEntry(String text){
        LOGGER.debug("Create word entry... ");
        List<String> definition = definitions.getOrDefault(text, List.of("No definition found"));
        return new Word(text, definition);
    }




    public int updatePageCounter(int lineCount, int pageCount){
        LOGGER.debug("Updating page counter...");
        if(lineCount % 40 != 0){
            return pageCount;
        }
        return ++pageCount;
    }
}
