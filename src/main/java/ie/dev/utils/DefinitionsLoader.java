package ie.dev.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DefinitionsLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionsLoader.class);

    public DefinitionsLoader(){

    }

    public Map<String, List<String>> load(Reader reader) throws Exception {
        LOGGER.info("Loading definitions...");

        CSVParser parser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build()
                    .parse(reader);

        return parser.stream()
                .collect(Collectors.groupingBy(
                        record -> record.get(0).toLowerCase(), TreeMap::new,
                        Collectors.mapping(
                                record -> record.get(1).toLowerCase() + "\t" + record.get(2).toLowerCase()+"\n",
                                Collectors.toList()
                        )
                ));

    }
}
