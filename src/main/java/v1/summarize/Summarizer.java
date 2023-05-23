package v1.summarize;

import v1.model.Document;
import v1.utils.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Summarizer {
    public static final String TEXT = "text";
    public static final String SOURCE = "source";
    public static final String CURRENT_SUMMARY = "currentSummary";
    public static final String INITIAL_SUMMARY = "initialSummary";

    public abstract String summarizeDocuments(final List<Document> documents,
                                     final Map<String, String> additionalParameters,
                                     final List<String> parametersFromDocumentMeta);

    public String summarize(final List<String> stringList,
                                     final Map<String, String> additionalParameters,
                                     final List<String> parametersFromDocumentMeta) {
        final List<Document> documents = stringList
                .stream().map(string -> Document.builder().text(string).build())
                .collect(Collectors.toList());
        return this.summarizeDocuments(documents,
                additionalParameters,
                parametersFromDocumentMeta);
    }

    public String summarize(final List<String> stringList) {
        return summarize(stringList, new HashMap<>(), new ArrayList<>());
    }

    public String summarizeDocuments(final List<Document> documents) {
        return summarizeDocuments(documents, new HashMap<>(), new ArrayList<>());
    }
}
