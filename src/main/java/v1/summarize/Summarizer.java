package v1.summarize;

import v1.model.Document;
import v1.utils.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Summarizer {
    public abstract String summarize(List<String> stringList, Map<String, String> additionalPromptKVs);

    public String summarize(final List<String> stringList, List<String> kvs) {
        return summarize(stringList, ListUtils.keyValuesToMap(kvs.toArray(new String[0])));
    }

    public String summarize(final List<String> stringList) {
        return summarize(stringList, new HashMap<>());
    }

    public abstract String summarizeWithSource(final List<Document> documents, Map<String, String> additionalPromptKVs);

    public String summarizeWithSource(final List<Document> documents, List<String> kvs) {
        return summarizeWithSource(documents, ListUtils.keyValuesToMap(kvs.toArray(new String[0])));
    }

    public String summarizeWithSource(final List<Document> documents) {
        return summarizeWithSource(documents, new HashMap<>());
    }
}
