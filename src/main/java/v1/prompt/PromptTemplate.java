package v1.prompt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import v1.model.Generative4jException;

import java.util.HashMap;
import java.util.Map;

/**
 * The PromptTemplate class is a utility class that provides a way to format a prompt string with dynamic values.
 * It has a constructor that takes a prompt template string and a format() method that takes a variable number of
 * key-value pairs and returns the formatted prompt string.
 */
@Builder
@Getter
@Setter
public class PromptTemplate {
    private String text;
    @Builder.Default
    private String openingString = "{";
    @Builder.Default
    private String closingString = "}";

    public PromptTemplate(final String text) {
        this(text, "{", "}");
    }

    public PromptTemplate(final String text, final String openingString, final String closingString) {
        this.text = text;
        this.openingString = openingString;
        this.closingString = closingString;
    }
    /**
     * Call the method with some key-value pairs.
     * Example: format("key1", "value1", "key2", "value2", "key3", "value3");
     * @param keyValuePairs the key-value pairs to use for formatting the prompt string.
     * @return the formatted prompt string.
     * @throws Generative4jException if the number of key-value pairs is not even.
     */
    public String format(final String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            new RuntimeException("Prompt parameter invalid, each key must have a value ");
            throw new Generative4jException("Prompt parameter invalid, each key must have a value ");
        }

        final HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            final String key = keyValuePairs[i];
            final String value = keyValuePairs[i + 1];
            hashMap.put(key, value);
        }

        return format(hashMap);
    }

    public String format(final Map<String, String> kevValues) {
        String result = text;

        for(String key : kevValues.keySet()) {
            CharSequence effectiveKey = openingString + key + closingString;
            result = result.replace(effectiveKey, kevValues.get(key).toString());
        }

        return result;
    }

    public PromptTemplate partialFormat(final String... keyValuePairs) {
        return PromptTemplate.builder()
                .text(format((keyValuePairs)))
                .build();
    }
}
