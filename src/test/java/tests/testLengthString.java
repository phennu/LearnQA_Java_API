package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class testLengthString {
    @ParameterizedTest
    @ValueSource(strings = {"Hello!", "Hello, my name is Ilyas"})
    public void checkLengthOfString(String name){
        assertTrue(name.length()>15, "Error, length must be more or equal 15 symbols");
    }
}
