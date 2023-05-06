package v1.summarize;

import java.util.List;

public abstract class Summarizer {
    public abstract String summarize(String initialSummary, List<String> stringList);


    public String summarize(final List<String> stringList) {
        return summarize("", stringList);
    }
}
