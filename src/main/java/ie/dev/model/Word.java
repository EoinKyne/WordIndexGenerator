package ie.dev.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public record Word (

        String word,
        List<String> definitions,
        Set<Integer> pageNumbers
) {

    public Word {
        definitions = List.copyOf(definitions);
        pageNumbers  = new TreeSet<>(pageNumbers);
    }
    public Word(String word, List<String> definition){
        this(word, definition, new TreeSet<>());
    }
    public void addPageNumber(int pageNumber) {
        pageNumbers.add(pageNumber);
    }
}
