package v1.summarize;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import v1.model.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummarizerTest {

    @Test
    void summarize() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarize(Mockito.anyList(), Mockito.anyMap()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarize(Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<String> input = Arrays.asList("test");
        String result = summarizer.summarize(Arrays.asList("test"));

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarize(Mockito.eq(input), Mockito.eq(new HashMap<>()));
    }

    @Test
    void summarizeKVs() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarize(Mockito.anyList(), Mockito.anyMap()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarize(Mockito.anyList(), Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<String> input = Arrays.asList("test");
        String result = summarizer.summarize(input, Arrays.asList("key", "value"));

        // Assert
        Assertions.assertEquals(result, "summary");
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        Mockito.verify(summarizer).summarize(Mockito.eq(input), Mockito.eq(map));
    }


    @Test
    void summarizeWithSource() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyList(), Mockito.anyMap()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<Document> input = Arrays.asList(Document.builder().text("text").source("source").build());
        String result = summarizer.summarizeWithSource(input);

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarizeWithSource(Mockito.eq(input), Mockito.eq(new HashMap<>()));
    }

    @Test
    void summarizeWithSourceKVs() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyList(), Mockito.anyMap()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarizeWithSource(Mockito.anyList(), Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        List<Document> input = Arrays.asList(Document.builder().text("text").source("source").build());
        String result = summarizer.summarizeWithSource(input, Arrays.asList("key", "value"));

        // Assert
        Assertions.assertEquals(result, "summary");
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        Mockito.verify(summarizer).summarizeWithSource(Mockito.eq(input), Mockito.eq(map));
    }
}