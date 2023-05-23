package v1.textsplitter;

import v1.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class TextSplitter {
    abstract List<String> split(String String, List<String> split);


    public List<Document> split(Document document, List<String> split) {
        return this.split(document.getText(), split)
                .stream()
                .map(s -> Document.builder()
                        .text(s)
                        .meta(Optional.ofNullable(document.getMeta()).map(TextSplitter::shallowCopy).orElse(null))
                        .build())
                .collect(Collectors.toList());
    }

    public List<Document> splitMany(List<Document> documents, List<String> split) {
        return documents.stream()
                .flatMap(document -> this.split(document, split).stream())
                .collect(Collectors.toList());
    }

    private static Map<String, Object> shallowCopy(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
