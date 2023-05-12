package v1.summarize;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import v1.model.Document;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummarizerTest {

    @Test
    void summarize() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarize(Mockito.anyString(), Mockito.anyList()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarize(Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<String> input = Arrays.asList("test");
        String result = summarizer.summarize(Arrays.asList("test"));

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarize(Mockito.eq(""), Mockito.eq(input));
    }

    @Test
    void summarizeWithSource() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyString(), Mockito.anyList()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<Document> input = Arrays.asList(Document.builder().text("text").source("source").build());
        String result = summarizer.summarizeWithSource(input);

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarizeWithSource(Mockito.eq(""), Mockito.eq(input));
    }
}