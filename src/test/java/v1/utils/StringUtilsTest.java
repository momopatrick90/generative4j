package v1.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void subStringCheckbounds() {
        //Arrange Act assert
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", -1,2), ".c");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 1, 4), "car");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 0,5), ".car");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", -1,5), ".car");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 3,2), "");
        Assertions.assertEquals(StringUtils.subStringCheckbounds(".car", 5,8), "");
    }
}