package v1.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import v1.model.Generative4jException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilsTest {

    @Test
    void keyValuesToMap() {
        // Arrange Act Assert
        Assertions.assertEquals(ListUtils.keyValuesToMap("1", "2"), new HashMap<String, String>() {{
            this.put("1", "2");
        }});
    }

    @Test
    void keyValuesToMapOddNumberOfKV() {
        // Arrange Act Assert
        Assertions.assertThrows(Generative4jException.class,
                () -> ListUtils.keyValuesToMap("1"));
    }

    @Test
    void keyValuesToMapObject() {
        // Arrange Act Assert
        Assertions.assertEquals(ListUtils.keyValuesToMapObject("1", "2"), new HashMap<String, String>() {{
            this.put("1", "2");
        }});
    }

}