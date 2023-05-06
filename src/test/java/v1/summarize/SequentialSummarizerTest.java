package v1.summarize;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import v1.aimodel.AIModel;
import v1.templatemodel.TemplateModel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SequentialSummarizerTest {
    @Test
    void summarize() {
        // Arrange
        String initialSummary = "initial";
        String string1 = "String 1";
        String string2 = "String 2";
        TemplateModel mockTemplateModel = Mockito.mock(TemplateModel.class);
        Mockito.when(mockTemplateModel.completion(Mockito.eq("currentSummary"), Mockito.eq(initialSummary), Mockito.eq("text"), Mockito.eq(string1))).thenReturn("Updated summary 1");
        Mockito.when(mockTemplateModel.completion(Mockito.eq("currentSummary"), Mockito.eq("Updated summary 1"), Mockito.eq("text"), Mockito.eq(string2))).thenReturn("Updated summary 2");

        SequentialSummarizer summarizer = SequentialSummarizer.builder()
                .templateModel(mockTemplateModel)
                .build();

        // Act
        String result = summarizer.summarize(initialSummary, Arrays.asList(string1, string2));

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
}