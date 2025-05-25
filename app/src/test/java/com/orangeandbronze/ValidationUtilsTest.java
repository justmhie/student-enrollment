package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testValidAlphanumeric() {
        assertTrue(ValidationUtils.isAlphanumeric("ABC123"));
        assertTrue(ValidationUtils.isAlphanumeric("test"));
        assertTrue(ValidationUtils.isAlphanumeric("123"));
        assertTrue(ValidationUtils.isAlphanumeric("Room101"));
    }

    @Test
    void testInvalidAlphanumeric() {
        assertFalse(ValidationUtils.isAlphanumeric("ABC-123"));
        assertFalse(ValidationUtils.isAlphanumeric("test space"));
        assertFalse(ValidationUtils.isAlphanumeric("123!"));
        assertFalse(ValidationUtils.isAlphanumeric(""));
        assertFalse(ValidationUtils.isAlphanumeric(null));
        assertFalse(ValidationUtils.isAlphanumeric("test@email.com"));
    }
}