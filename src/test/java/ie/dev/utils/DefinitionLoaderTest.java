package ie.dev.utils;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class DefinitionLoaderTest {
    DefinitionsLoader loader;
    @Test
    void loadsSingleWordCorrectly() throws Exception {
        String csv = "\"word\",\"wordtype\",\"definition\"\n" +
                "\"A\",\"noun\",\"first letter\"\n";



        Map<String, List<String>> result = loader.load(new StringReader(csv));
    }
}
