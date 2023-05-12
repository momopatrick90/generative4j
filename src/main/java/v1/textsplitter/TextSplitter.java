package v1.textsplitter;

import v1.model.Document;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TextSplitter {
    abstract List<String> split(String String, List<String> split);


    public List<Document> split(Document document, List<String> split) {
        return this.split(document.getText(), split)
                .stream()
                .map(s -> Document.builder()
                        .text(s)
                        .source(document.getSource())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Document> splitMany(List<Document> documents, List<String> split) {
        return documents.stream()
                .flatMap(document -> this.split(document, split).stream())
                .collect(Collectors.toList());
    }
}
