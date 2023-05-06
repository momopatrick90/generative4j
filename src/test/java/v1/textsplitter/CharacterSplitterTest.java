package v1.textsplitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import v1.model.Generative4jException;

import java.util.Arrays;

class CharacterSplitterTest {


    @Test
    public void split() {
        // Arrange Act Assert
        Assertions.assertEquals( new CharacterSplitter(2)
                        .split("0102", Arrays.asList(".")),
                Arrays.asList("01", "02"));
        Assertions.assertEquals( new CharacterSplitter(10)
                        .split("example.com/image.png", Arrays.asList(".")),
                Arrays.asList("example.", "com/image.", "png"));
        Assertions.assertEquals( new CharacterSplitter(19)
                .split("example.com/image.png",  Arrays.asList(".")),
                Arrays.asList("example.com/image.", "png"));
        Assertions.assertEquals( new CharacterSplitter(7)
                        .split("example.com",  Arrays.asList(".")),
                Arrays.asList("example.", "com"));
    }

    @Test
    public void splitUsesLongestMatching() {
        // Arrange Act Assert
        Assertions.assertEquals( new CharacterSplitter(10)
                        .split("example. com",  Arrays.asList(".", ". ")),
                Arrays.asList("example. ", "com"));
    }

    @Test
    public void substringUntilLimitLongerThanText() {
        // Arrange Act Assert
        Assertions.assertEquals( new CharacterSplitter(1000)
                        .split("example.com",  Arrays.asList(".")),
                Arrays.asList("example.com"));
    }

    @Test
    public void substringUntilLimitLess1() {
        // Arrange Act Assert
        Assertions.assertThrows(Generative4jException.class,
                () -> new CharacterSplitter(0)
                        .split("example.com",  Arrays.asList(".")) );
    }
}