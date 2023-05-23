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
    /**
     * The template model used to generate the summary, it has to parameters:
     * {currentSummary} representing the current summary.
     * {text} representing the text to be added to the current summary.
     */
    TemplateModel templateModel;

    @Override
    public String summarizeDocuments(final List<Document> documents,
                                    final Map<String, String> additionalParameters,
                                    final List<String> parametersFromDocumentMeta) {
        String currentSummary = additionalParameters.getOrDefault(INITIAL_SUMMARY, "");

        for (final Document document : documents) {
            final Map<String, String> parameters = new HashMap<>(additionalParameters);
            parameters.put(TEXT, document.getText());
            parameters.put(CURRENT_SUMMARY, currentSummary);
            for(final String parameter : parametersFromDocumentMeta) {
                parameters.put(parameter, (String) document.getMeta().get(parameter));
            }

            currentSummary = templateModel.completion(parameters);

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
