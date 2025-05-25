package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {

    @Test
    void testSubjectCreation() {
        Subject subject = new Subject("MATH101", 3, false);
        
        assertEquals("MATH101", subject.getSubjectId());
        assertEquals(3, subject.getUnits());
        assertFalse(subject.isLaboratory());
        assertTrue(subject.getPrerequisites().isEmpty());
    }

    @Test
    void testLaboratorySubject() {
        Subject labSubject = new Subject("CHEM101L", 1, true);
        
        assertTrue(labSubject.isLaboratory());
    }

    @Test
    void testInvalidSubjectId() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Subject("MATH-101", 3, false));
    }

    @Test
    void testInvalidUnits() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Subject("MATH101", 0, false));
        assertThrows(IllegalArgumentException.class, 
            () -> new Subject("MATH101", -1, false));
    }

    @Test
    void testPrerequisites() {
        Subject basicMath = new Subject("MATH101", 3, false);
        Subject advancedMath = new Subject("MATH201", 3, false);
        
        advancedMath.addPrerequisite(basicMath);
        
        assertTrue(advancedMath.getPrerequisites().contains(basicMath));
        assertEquals(1, advancedMath.getPrerequisites().size());
    }
}