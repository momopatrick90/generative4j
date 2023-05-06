package v1.prompt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import v1.model.Generative4jException;

import java.util.HashMap;

class PromptTemplateTest {
    @Test
    public void testFormatWithOneKeyValuePair() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Hello {name}!");
        final String expected = "Hello Alice!";

        // Act
        String actual = prompt.format("name", "Alice");

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithMultipleKeyValuePairs() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Welcome to {city}, {name}!");
        String expected = "Welcome to New York, Alice!";

        // Act
        String actual = prompt.format("city", "New York", "name", "Alice");

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test()
    public void testFormatWithInvalidKeyValuePairs() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Welcome to {key}");

        // Act Assert
        Assertions.assertThrows(Generative4jException.class, () -> prompt.format("key"));
    }

    @Test
    public void testFormatMap() {
        // Arrange
        final PromptTemplate prompt = new PromptTemplate("Hello {name} {name}!");
        final String expected = "Hello Alice Alice!";

        // Act
        final HashMap<String, String> map = new HashMap<>();
        map.put("name", "Alice");
        String actual = prompt.format(map);

        // Assert
        Assertions.assertEquals(expected, actual);
    }
}