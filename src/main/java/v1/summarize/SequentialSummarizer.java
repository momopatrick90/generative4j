package v1.summarize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import v1.aimodel.AIModel;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class SequentialSummarizer extends Summarizer{
    /**
     * The template model used to generate the summary, it has to parameters:
     * {currentSummary} representing the current summary.
     * {text} representing the text to be added to the current summary.
     */
    TemplateModel templateModel;

    @Override
    public String summarize(final String initialSummary, final List<String> stringList) {
        String currentSummary = initialSummary;

        for (final String string : stringList) {
            currentSummary = templateModel.completion("currentSummary", currentSummary, "text", string);
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
}
