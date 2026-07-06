package ie.dev.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class IgnoreWordsLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreWordsLoader.class);
    public IgnoreWordsLoader(){

    }

    public Set<String> load() throws IOException {
        LOGGER.info("Loading ignorewords...");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        IgnoreWordsLoader.class.getResourceAsStream("/templates/IgnoreWords.txt")
                )
        )){
            return br.lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    }
}
