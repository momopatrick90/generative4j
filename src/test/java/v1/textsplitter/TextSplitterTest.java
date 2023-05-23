package v1.textsplitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import v1.model.Document;
import v1.summarize.Summarizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextSplitterTest {

    @Test
    void splitDocument() {
        // Arrange
        TextSplitter textSplitter = Mockito.mock(TextSplitter.class);
        Mockito.when(textSplitter.split(Mockito.anyString(), Mockito.anyList()))
                        .thenReturn(Arrays.asList("part1", "part2"));

        Mockito.when(textSplitter.split(Mockito.any(Document.class), Mockito.anyList()))
                .thenCallRealMethod();
        Document document = Document.builder()
                .text("text")
                .meta(new HashMap<String, Object>() {{
                    put(Summarizer.SOURCE, "source1");
                }})
                .build();

        // Act
        List<Document> documents = textSplitter.split(document, Arrays.asList("part1", "part2"));

        // Assert
        Assertions.assertEquals(documents.get(0).getText(), "part1");
        Assertions.assertEquals(documents.get(0).getMeta(), new HashMap<String, Object>() {{
            put(Summarizer.SOURCE, "source1");
        }});
        Assertions.assertEquals(documents.get(1).getText(), "part2");
        Assertions.assertEquals(documents.get(1).getMeta(), new HashMap<String, Object>() {{
            put(Summarizer.SOURCE, "source1");
        }});
    }

    @Test
    void splitMany() {
        // Arrange
        TextSplitter textSplitter = Mockito.mock(TextSplitter.class);
        Mockito.when(textSplitter.split(Mockito.anyString(), Mockito.anyList()))
                .thenReturn(Arrays.asList("part1", "part2"));

        Mockito.when(textSplitter.split(Mockito.any(Document.class), Mockito.anyList()))
                .thenCallRealMethod();
        Mockito.when(textSplitter.splitMany(Mockito.anyList(), Mockito.anyList()))
                .thenCallRealMethod();
        Document document = Document.builder()
                .text("text")
                .meta(new HashMap<String, Object>() {{
                    put(Summarizer.SOURCE, "source1");
                }})
                .build();

        // Act
        List<Document> documents = textSplitter.splitMany(Arrays.asList(document), Arrays.asList("part1", "part2"));

        // Assert
        Assertions.assertEquals(documents.get(0).getText(), "part1");
        Assertions.assertEquals(documents.get(0).getMeta(), new HashMap<String, Object>() {{
            put(Summarizer.SOURCE, "source1");
        }});
        Assertions.assertEquals(documents.get(1).getText(), "part2");
        Assertions.assertEquals(documents.get(1).getMeta(), new HashMap<String, Object>() {{
            put(Summarizer.SOURCE, "source1");
        }});
    }
}