package v1.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void subStringCheckbounds() {
        //Arrange Act assert
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 2), ".c");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 4), ".car");
    }

    @Test
    void subStringCheckboundsStringShort() {
        //Arrange Act assert
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 5), "");
    }

    @Test
    void subStringCheckboundsLongNull() {
        //Arrange Act assert
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", null), "");
    }
}