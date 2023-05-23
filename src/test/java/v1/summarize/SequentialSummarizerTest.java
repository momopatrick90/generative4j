package v1.summarize;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import v1.aimodel.AIModel;
import v1.model.Document;
import v1.templatemodel.TemplateModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SequentialSummarizerTest {
    @Test
    void summarizeDocuments() {
        // Arrange
        String initialSummary = "initial";
        String string1 = "String 1";
        String string2 = "String 2";
        TemplateModel mockTemplateModel = Mockito.mock(TemplateModel.class);

        HashMap<String,String> kvs1 = new HashMap<>();
        kvs1.put("currentSummary", initialSummary);
        kvs1.put("text", string1);
        kvs1.put("initialSummary", initialSummary);
        kvs1.put("source", "source1");
        Mockito.when(mockTemplateModel.completion(Mockito.argThat(new MapEquals(kvs1)))).thenReturn("Updated summary 1");

        HashMap<String,String> kvs2 = new HashMap<>();
        kvs2.put("currentSummary", "Updated summary 1");
        kvs2.put("text", string2);
        kvs2.put("initialSummary", initialSummary);
        kvs2.put("source", "source2");
        Mockito.when(mockTemplateModel.completion(Mockito.argThat(new MapEquals(kvs2)))).thenReturn("Updated summary 2");

        SequentialSummarizer summarizer = SequentialSummarizer.builder()
                .templateModel(mockTemplateModel)
                .build();

        // Act
        Document document1 = Document.builder()
                .text(string1)
                .meta(new HashMap<String, Object>() {{
                    put(Summarizer.SOURCE, "source1");
                }})
                .build();
        Document document2 = Document.builder()
                .text(string2)
                .meta(new HashMap<String, Object>() {{
                    put(Summarizer.SOURCE, "source2");
                }})
                .build();
        final HashMap<String, String> parameters = new HashMap<>();
        parameters.put("initialSummary", initialSummary);
        String result = summarizer.summarizeDocuments(Arrays.asList(document1, document2),
                parameters, Arrays.asList(Summarizer.SOURCE));

        // Assert
        assertEquals("Updated summary 2", result);
    }

    @Test
    void createDefault() {
        // Arrange Act
        final AIModel aiModel = new AIModel();
        final SequentialSummarizer summarizer = SequentialSummarizer.createDefault(aiModel);

        // Assert
        Assertions.assertEquals(summarizer.getTemplateModel().getPromptTemplate().getText(),
                SequentialSummarizer.DEFAULT_PROMPT);
        Assertions.assertEquals(summarizer.getTemplateModel().getAiModel(),
                aiModel);
    }

    @Test
    void createDefaultWithSource() {
        // Arrange Act
        final AIModel aiModel = new AIModel();
        final SequentialSummarizer summarizer = SequentialSummarizer.createDefaultWithSource(aiModel);

        // Assert
        Assertions.assertEquals(summarizer.getTemplateModel().getPromptTemplate().getText(),
                SequentialSummarizer.DEFAULT_PROMPT_SOURCE);
        Assertions.assertEquals(summarizer.getTemplateModel().getAiModel(),
                aiModel);
    }

    @AllArgsConstructor
    public static class MapEquals extends ArgumentMatcher<Map> {
        public  Map<String, String> map;

        @Override
        public boolean matches(Object argument) {
            return map.equals(argument);
        }
    }
}