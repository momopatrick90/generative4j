package v1.summarize;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import v1.model.Document;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SummarizerTest {

    @Test
    void summarize() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeDocuments(Mockito.anyList(), Mockito.anyMap(), Mockito.anyList()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarize(Mockito.anyList(), Mockito.anyMap(), Mockito.anyList()))
                .thenCallRealMethod();
        Mockito.when(summarizer.summarize(Mockito.anyList()))
                .thenCallRealMethod();
        List<String> input = Arrays.asList("test");

        // Act
        String result = summarizer.summarize(input);

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarizeDocuments(
                Mockito.argThat(new FirstDocEquals(Arrays.asList(Document.builder().text("test").build()))),
                Mockito.eq(new HashMap<>()),
                Mockito.eq(new ArrayList<>()));
    }

    @Test
    void summarizeDocumentsListOnly() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeDocuments(Mockito.anyList(), Mockito.anyMap(), Mockito.anyList()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarizeDocuments(Mockito.anyList()))
                .thenCallRealMethod();
        List<Document> input = Arrays.asList(Document.builder().text("test").build());

        // Act
        String result = summarizer.summarizeDocuments(input);

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarizeDocuments(Mockito.eq(input),
                Mockito.eq(new HashMap<>()),
                Mockito.eq(new ArrayList<>()));
    }

    @Test
    void summarizeAdditionParamsAndMeta() {
        // Arrange
        final Summarizer summarizer = Mockito.mock(Summarizer.class);
        Mockito.when(summarizer.summarizeDocuments(Mockito.anyList(), Mockito.anyMap(), Mockito.anyList()))
                .thenReturn("summary");
        Mockito.when(summarizer.summarize(Mockito.anyList(), Mockito.anyMap(), Mockito.anyList()))
                .thenCallRealMethod();

        // Act
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        String result = summarizer.summarize( Arrays.asList("test"), map, Arrays.asList("metaKey"));

        // Assert
        Assertions.assertEquals(result, "summary");
        Mockito.verify(summarizer).summarizeDocuments(
                Mockito.argThat(new FirstDocEquals(Arrays.asList(Document.builder().text("test").build()))),
                Mockito.eq(map),
                Mockito.eq(Arrays.asList("metaKey")));
    }

    @AllArgsConstructor
    public static class FirstDocEquals extends ArgumentMatcher<List<Document>> {
        public  List<Document> document;

        @Override
        public boolean matches(Object argument) {
            return document.get(0).getText().equals((((List<Document>)argument).get(0).getText()));
        }
    }
}