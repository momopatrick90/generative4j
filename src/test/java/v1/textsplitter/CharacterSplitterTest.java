package v1.textsplitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import v1.model.Generative4jException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSplitterTest {


    @Test
    public void split() {
        // Arrange Act Assert
        Assertions.assertEquals( new CharacterLimitSplitter(2)
                        .split("0102", "\\."),
                Arrays.asList("01", "02"));
        Assertions.assertEquals( new CharacterLimitSplitter(10)
                        .split("example.com/image.png", "\\."),
                Arrays.asList("example.", "com/image.", "png"));
        Assertions.assertEquals( new CharacterLimitSplitter(19)
                .split("example.com/image.png", "\\."),
                Arrays.asList("example.com/image.", "png"));
        Assertions.assertEquals( new CharacterLimitSplitter(7)
                        .split("example.com", "\\."),
                Arrays.asList("example.", "com"));
    }

    @Test
    public void substringUntilLimitLongerThanText() {
        // Arrange Act Assert
        Assertions.assertEquals( new CharacterLimitSplitter(1000)
                        .split("example.com", "\\."),
                Arrays.asList("example.com"));
    }

    @Test
    public void substringUntilLimitLess1() {
        // Arrange Act Assert
        Assertions.assertThrows(Generative4jException.class,
                () -> new CharacterLimitSplitter(0)
                        .split("example.com", "\\.") );
    }
}