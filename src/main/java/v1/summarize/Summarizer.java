package v1.summarize;

import v1.model.Document;

import java.util.List;

public abstract class Summarizer {
    public abstract String summarize(String initialSummary, List<String> stringList);


    public String summarize(final List<String> stringList) {
        return summarize("", stringList);
    }

    public abstract String summarizeWithSource(final String initialSummary, final List<Document> documents);

    public String summarizeWithSource(final List<Document> documents) {
        return summarizeWithSource("", documents);
    }
}
