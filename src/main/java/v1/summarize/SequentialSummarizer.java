package v1.summarize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import v1.aimodel.AIModel;
import v1.model.Document;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
@Slf4j
public class SequentialSummarizer extends Summarizer {
    public static final String TEXT = "text";
    public static final String SOURCE = "source";
    public static final String CURRENT_SUMMARY = "currentSummary";
    public static final String INITIAL_SUMMARY = "initialSummary";
    /**
     * The template model used to generate the summary, it has to parameters:
     * {currentSummary} representing the current summary.
     * {text} representing the text to be added to the current summary.
     */
    TemplateModel templateModel;

    @Override
    public String summarize(final List<String> stringList, final Map<String, String> additionalPromptKVs) {
        String currentSummary = additionalPromptKVs.getOrDefault(INITIAL_SUMMARY, "");

        for (final String string : stringList) {
            final Map<String, String> kvs = new HashMap<>(additionalPromptKVs);
            kvs.put(TEXT, string);
            kvs.put(CURRENT_SUMMARY, currentSummary);

            currentSummary = templateModel.completion(kvs);

        }

        return currentSummary;
    }

    @Override
    public String summarizeWithSource(final List<Document> stringList , final Map<String, String> additionalPromptKVs) {
        String currentSummary = additionalPromptKVs.getOrDefault(INITIAL_SUMMARY, "");

        for (final Document textAndSource : stringList) {
            final Map<String, String> kvs = new HashMap<>(additionalPromptKVs);
            kvs.put(TEXT, textAndSource.getText());
            kvs.put(SOURCE, textAndSource.getSource());
            kvs.put(CURRENT_SUMMARY, currentSummary);

            currentSummary = templateModel.completion(kvs);
        }

        return currentSummary;
    }

    public static SequentialSummarizer createDefault(final AIModel aiModel) {
        return SequentialSummarizer.builder()
                .templateModel(TemplateModel.builder()
                        .aiModel(aiModel)
                        .promptTemplate(PromptTemplate.builder()
                                .text(DEFAULT_PROMPT)
                                .build())
                        .build())
                .build();
    }

    public static SequentialSummarizer createDefaultWithSource(final AIModel aiModel) {
        return SequentialSummarizer.builder()
                .templateModel(TemplateModel.builder()
                        .aiModel(aiModel)
                        .promptTemplate(PromptTemplate.builder()
                                .text(DEFAULT_PROMPT_SOURCE)
                                .build())
                        .build())
                .build();
    }


    // TODO improve
    public static final String DEFAULT_PROMPT = "Write a concise summary of the following texts in triple backticks:" +
            "\n" +
            "```" +
            "{currentSummary}" +
            "```" +
            "\n" +
            "```" +
            "{text}" +
            "```" +
            "CONCISE SUMMARY:";


    public static final String DEFAULT_PROMPT_SOURCE = "Write a concise summary of the following texts in triple backticks:" +
            "\n" +
            "```" +
            "{currentSummary}" +
            "```" +
            "\n" +
            "```" +
            "{text}" +
            "```" +
            "CONCISE SUMMARY:";
}
