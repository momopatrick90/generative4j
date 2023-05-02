package v1.prompt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import v1.model.Generative4jException;
import v1.model.PromptTemplate;

import java.util.HashMap;

class PromptTemplateRendererTest {
    @Test
    public void testFormatWithOneKeyValuePair() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Hello {name}!");
        final String expected = "Hello Alice!";

        // Act
        String actual = PromptTemplateRenderer.format(prompt, "name", "Alice");

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMultipleKeyValuePairs() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Welcome to {city}, {name}!");
        String expected = "Welcome to New York, Alice!";

        // Act
        String actual = PromptTemplateRenderer.format(prompt,"city", "New York", "name", "Alice");

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test()
    public void testFormatWithInvalidKeyValuePairs() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Welcome to {key}");

        // Act Assert
        Assertions.assertThrows(Generative4jException.class, () -> PromptTemplateRenderer.format(prompt,"key"));
    }

    @Test
    public void testFormatMap() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Hello {name} {name}!");
        final String expected = "Hello Alice Alice!";

        // Act
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        String actual = PromptTemplateRenderer.format(prompt, map);

        // Assert
        Assertions.assertEquals(expected, actual);
    }
}