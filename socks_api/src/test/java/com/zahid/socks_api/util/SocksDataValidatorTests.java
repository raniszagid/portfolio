package com.zahid.socks_api.util;

import com.zahid.socks_api.dto.SocksDto;
import com.zahid.socks_api.exceptions.SocksDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SocksDataValidatorTests {
    private final SocksDataValidator validator;

    @Autowired
    public SocksDataValidatorTests(SocksDataValidator validator) {
        this.validator = validator;
    }

    @Test
    public void test_checkCottonValues() {
        assertTrue(validator.checkCottonValues(null));
        assertTrue(validator.checkCottonValues(50));
        assertFalse(validator.checkCottonValues(101));
        assertFalse(validator.checkCottonValues(-1));
    }

    @Test
    public void checkInputData_NullColor_ThrowSocksDataException() {
        SocksDto example = new SocksDto();
        Executable executable = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, executable);
    }

    @Test
    public void checkInputData_NegativeQuantity_ThrowSocksDataException() {
        SocksDto example = new SocksDto();
        example.setColor("green");
        example.setCotton(55);
        example.setQuantity(-4);
        Executable executable = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, executable);
    }

    @Test
    public void checkInputData_NullQuantity_ThrowSocksDataException() {
        SocksDto example = new SocksDto();
        example.setColor("yellow");
        example.setCotton(44);
        example.setQuantity(0);
        Executable updatingCase = () -> validator.checkInputData(example, false);
        Executable addingCase = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, addingCase);
        assertDoesNotThrow(updatingCase);
    }

    @Test
    public void checkInputData_NullCotton_ThrowSocksDataException() {
        SocksDto example = new SocksDto();
        example.setColor("yellow");
        example.setCotton(0);
        example.setQuantity(5);
        Executable updatingCase = () -> validator.checkInputData(example, false);
        Executable addingCase = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, addingCase);
        assertDoesNotThrow(updatingCase);
    }

    @Test
    public void checkInputData_WrongCottonValue_ThrowSocksDataException() {
        SocksDto example = new SocksDto();
        example.setColor("green");
        example.setCotton(-1);
        example.setQuantity(77);
        Executable executable = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, executable);
        example.setCotton(101);
        executable = () -> validator.checkInputData(example, true);
        assertThrows(SocksDataException.class, executable);
    }

    @Test
    public void test_checkFilterParameters() {
        Executable executable = () -> validator.checkFilterParameters(null, 23);
        assertDoesNotThrow(executable);
        executable = () -> validator.checkFilterParameters(null, null);
        assertDoesNotThrow(executable);
        executable = () -> validator.checkFilterParameters(23, 29);
        assertDoesNotThrow(executable);

        executable = () -> validator.checkFilterParameters(99, 1);
        assertThrows(SocksDataException.class, executable);
        executable = () -> validator.checkFilterParameters(80, 101);
        assertThrows(SocksDataException.class, executable);
        executable = () -> validator.checkFilterParameters(-4, 10);
        assertThrows(SocksDataException.class, executable);
    }

    @Test
    public void test_checkCountingParameters() {
        Executable executable = () -> validator.checkCountingParameters(null, 24, null);
        assertDoesNotThrow(executable);

        executable = () -> validator.checkCountingParameters(99, 1, null);
        assertThrows(SocksDataException.class, executable);
        executable = () -> validator.checkCountingParameters(80, -11, 66);
        assertThrows(SocksDataException.class, executable);
        executable = () -> validator.checkCountingParameters(882, 10, 732);
        assertThrows(SocksDataException.class, executable);
    }
}
