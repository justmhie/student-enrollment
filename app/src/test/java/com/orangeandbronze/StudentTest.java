package com.orangeandbronze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import com.orangeandbronze.exceptions.*;

class StudentTest {
    private Student student;
    private Subject mathSubject;
    private Subject physicsSubject;
    private Subject labSubject;
    private Room room;
    private Instructor instructor;
    private Schedule schedule1;
    private Schedule schedule2;

    @BeforeEach
    void setUp() {
        student = new Student(12345);
        mathSubject = new Subject("MATH101", 3, false);
        physicsSubject = new Subject("PHYS101", 4, false);
        labSubject = new Subject("CHEM101L", 1, true);
        room = new Room("A101", 30);
        instructor = new Instructor("Dr. Smith");
        schedule1 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        schedule2 = new Schedule(Schedule.Days.TF, Schedule.Period.H1000_1130);
    }

    @Test
    void testStudentCreation() {
        assertEquals(12345, student.getStudentNumber());
        assertTrue(student.getEnrolledSections().isEmpty());
        assertTrue(student.getCompletedSubjects().isEmpty());
    }

    @Test
    void testInvalidStudentNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Student(-1));
    }

    @Test
    void testSuccessfulEnlistment() throws EnlistmentException {
        Section section = new Section("SEC001", mathSubject, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        student.enlist(section);
        
        assertTrue(student.getEnrolledSections().contains(section));
        assertEquals(1, student.getEnrolledSections().size());
    }

    @Test
    void testDuplicateEnlistment() throws EnlistmentException {
        Section section = new Section("SEC001", mathSubject, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        student.enlist(section);
        
        assertThrows(EnlistmentException.class, () -> student.enlist(section));
    }

    @Test
    void testScheduleConflict() throws EnlistmentException {
        Section section1 = new Section("SEC001", mathSubject, schedule1, room, instructor);
        Section section2 = new Section("SEC002", physicsSubject, schedule1, new Room("B101", 25), new Instructor("Dr. Jones"));
        
        room.assignSection(section1);
        instructor.assignSection(section1);
        
        student.enlist(section1);
        
        assertThrows(ScheduleConflictException.class, () -> student.enlist(section2));
    }

    @Test
    void testSameSubjectEnrollment() throws EnlistmentException {
        Section section1 = new Section("SEC001", mathSubject, schedule1, room, instructor);
        Section section2 = new Section("SEC002", mathSubject, schedule2, new Room("B101", 25), new Instructor("Dr. Jones"));
        
        room.assignSection(section1);
        instructor.assignSection(section1);
        
        student.enlist(section1);
        
        assertThrows(SameSubjectEnrollmentException.class, () -> student.enlist(section2));
    }

    @Test
    void testPrerequisiteNotMet() throws ScheduleConflictException {
        Subject advancedMath = new Subject("MATH201", 3, false);
        advancedMath.addPrerequisite(mathSubject);
        
        Section section = new Section("SEC001", advancedMath, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        assertThrows(PrerequisiteNotMetException.class, () -> student.enlist(section));
    }

    @Test
    void testPrerequisiteMet() throws EnlistmentException {
        Subject advancedMath = new Subject("MATH201", 3, false);
        advancedMath.addPrerequisite(mathSubject);
        
        student.completeSubject(mathSubject);
        
        Section section = new Section("SEC001", advancedMath, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        student.enlist(section);
        
        assertTrue(student.getEnrolledSections().contains(section));
    }

    @Test
    void testCapacityReached() throws EnlistmentException {
        Room smallRoom = new Room("SMALL1", 1);
        Section section = new Section("SEC001", mathSubject, schedule1, smallRoom, instructor);
        smallRoom.assignSection(section);
        instructor.assignSection(section);
        
        Student student2 = new Student(67890);
        student.enlist(section);
        
        assertThrows(CapacityReachedException.class, () -> student2.enlist(section));
    }

    @Test
    void testCancelEnlistment() throws EnlistmentException {
        Section section = new Section("SEC001", mathSubject, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        student.enlist(section);
        assertTrue(student.getEnrolledSections().contains(section));
        
        student.cancel(section);
        assertFalse(student.getEnrolledSections().contains(section));
    }

    @Test
    void testCancelNonEnlistedSection() throws ScheduleConflictException {
        Section section = new Section("SEC001", mathSubject, schedule1, room, instructor);
        room.assignSection(section);
        instructor.assignSection(section);
        
        assertThrows(EnlistmentException.class, () -> student.cancel(section));
    }

    @Test
    void testAssessmentCalculation() throws EnlistmentException {
        // Create sections with different subjects
        Section mathSection = new Section("SEC001", mathSubject, schedule1, room, instructor);
        Section labSection = new Section("SEC002", labSubject, schedule2, new Room("LAB1", 20), new Instructor("Dr. Lab"));
        
        room.assignSection(mathSection);
        instructor.assignSection(mathSection);
        
        student.enlist(mathSection);
        student.enlist(labSection);
        
        BigDecimal assessment = student.requestAssessment();
        
        // Expected calculation:
        // Units: 3 (math) + 1 (lab) = 4 units
        // Unit fee: 4 * 2345.67 = 9382.68
        // Lab fee: 1 * 1234.56 = 1234.56
        // Misc fee: 3456.78
        // Subtotal: 9382.68 + 1234.56 + 3456.78 = 14074.02
        // VAT (12%): 14074.02 * 0.12 = 1688.88
        // Total: 14074.02 + 1688.88 = 15762.90
        
        BigDecimal expected = new BigDecimal("15762.90");
        assertEquals(0, expected.compareTo(assessment));
    }

    @Test
    void testAssessmentWithNoEnrollment() {
        BigDecimal assessment = student.requestAssessment();
        
        // Only misc fee + VAT
        // Misc fee: 3456.78
        // VAT: 3456.78 * 0.12 = 414.81
        // Total: 3456.78 + 414.81 = 3871.59
        
        BigDecimal expected = new BigDecimal("3871.59");
        assertEquals(0, expected.compareTo(assessment));
    }
}