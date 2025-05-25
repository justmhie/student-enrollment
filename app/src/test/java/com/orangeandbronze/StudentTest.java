package com.orangeandbronze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    
    private Student student;
    private Section section1;
    private Section section2;
    private Section conflictingSection;
    private Section fullSection;
    
    @BeforeEach
    public void setUp() {
        student = new Student(12345);
        
        Schedule schedule1 = new Schedule("Mon/Thu", "8:30am-10am");
        Schedule schedule2 = new Schedule("Tue/Fri", "10am-11:30am");
        Schedule conflictingSchedule = new Schedule("Mon/Thu", "8:30am-10am");
        
        Room room1 = new Room("A101", 30);
        Room room2 = new Room("A102", 25);
        Room smallRoom = new Room("A103", 1);
        
        section1 = new Section("MATH101", schedule1, room1);
        section2 = new Section("PHYS101", schedule2, room2);
        conflictingSection = new Section("CHEM101", conflictingSchedule, room2);
        fullSection = new Section("CS101", schedule2, smallRoom);
        
        // Fill up the full section
        Student otherStudent = new Student(99999);
        otherStudent.enlist(fullSection);
    }
    
    @Test
    public void testValidStudentCreation() {
        assertEquals(12345, student.getStudentNumber());
        assertEquals(0, student.getEnrolledSectionCount());
        assertTrue(student.getEnrolledSections().isEmpty());
    }
    
    @Test
    public void testNegativeStudentNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student(-1);
        });
    }
    
    @Test
    public void testZeroStudentNumberIsValid() {
        Student zeroStudent = new Student(0);
        assertEquals(0, zeroStudent.getStudentNumber());
    }
    
    @Test
    public void testSuccessfulEnlistment() {
        student.enlist(section1);
        
        assertTrue(student.isEnrolledIn(section1));
        assertEquals(1, student.getEnrolledSectionCount());
        assertTrue(student.getEnrolledSections().contains(section1));
        assertTrue(section1.isStudentEnrolled(student));
    }
    
    @Test
    public void testMultipleEnlistments() {
        student.enlist(section1);
        student.enlist(section2);
        
        assertTrue(student.isEnrolledIn(section1));
        assertTrue(student.isEnrolledIn(section2));
        assertEquals(2, student.getEnrolledSectionCount());
    }
    
    @Test
    public void testDuplicateEnlistmentThrowsException() {
        student.enlist(section1);
        
        assertThrows(IllegalStateException.class, () -> {
            student.enlist(section1);
        });
    }
    
    @Test
    public void testScheduleConflictPreventsEnlistment() {
        student.enlist(section1);
        
        assertThrows(IllegalStateException.class, () -> {
            student.enlist(conflictingSection);
        });
    }
    
    @Test
    public void testCapacityLimitPreventsEnlistment() {
        assertThrows(IllegalStateException.class, () -> {
            student.enlist(fullSection);
        });
    }
    
    @Test
    public void testNullSectionEnlistmentThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            student.enlist(null);
        });
    }
    
    @Test
    public void testSuccessfulCancellation() {
        student.enlist(section1);
        student.enlist(section2);
        
        student.cancel(section1);
        
        assertFalse(student.isEnrolledIn(section1));
        assertTrue(student.isEnrolledIn(section2));
        assertEquals(1, student.getEnrolledSectionCount());
        assertFalse(section1.isStudentEnrolled(student));
    }
    
    @Test
    public void testCancellingNonEnrolledSectionThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            student.cancel(section1);
        });
    }
    
    @Test
    public void testNullSectionCancellationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            student.cancel(null);
        });
    }
    
    @Test
    public void testStudentEquality() {
        Student student1 = new Student(12345);
        Student student2 = new Student(12345);
        Student student3 = new Student(54321);
        
        assertEquals(student1, student2);
        assertNotEquals(student1, student3);
    }
    
    @Test
    public void testEnlistmentAfterCancellation() {
        student.enlist(section1);
        student.cancel(section1);
        
        // Should be able to enlist again after cancellation
        assertDoesNotThrow(() -> {
            student.enlist(section1);
        });
        
        assertTrue(student.isEnrolledIn(section1));
    }
}