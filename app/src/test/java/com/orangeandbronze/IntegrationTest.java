package com.orangeandbronze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import com.orangeandbronze.exceptions.*;

class IntegrationTest {
    private Student student1, student2;
    private Subject math101, math201, phys101, chem101L;
    private Room roomA101, roomB101, labRoom;
    private Instructor drSmith, drJones, drLab;
    private Schedule scheduleMTH830, scheduleTF830, scheduleWS1000;

    @BeforeEach
    void setUp() {
        // Students
        student1 = new Student(12345);
        student2 = new Student(67890);

        // Subjects
        math101 = new Subject("MATH101", 3, false);
        math201 = new Subject("MATH201", 3, false);
        phys101 = new Subject("PHYS101", 4, false);
        chem101L = new Subject("CHEM101L", 1, true);
        
        // Set prerequisites
        math201.addPrerequisite(math101);

        // Rooms
        roomA101 = new Room("A101", 30);
        roomB101 = new Room("B101", 25);
        labRoom = new Room("LAB1", 15);

        // Instructors
        drSmith = new Instructor("Dr. Smith");
        drJones = new Instructor("Dr. Jones");
        drLab = new Instructor("Dr. Lab");

        // Schedules
        scheduleMTH830 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        scheduleTF830 = new Schedule(Schedule.Days.TF, Schedule.Period.H0830_1000);
        scheduleWS1000 = new Schedule(Schedule.Days.WS, Schedule.Period.H1000_1130);
    }

    @Test
    void testCompleteEnrollmentWorkflow() throws EnlistmentException {
        // Create sections
        Section mathSection = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        Section physSection = new Section("PHYS101A", phys101, scheduleTF830, roomB101, drJones);
        Section labSection = new Section("CHEM101LA", chem101L, scheduleWS1000, labRoom, drLab);

        // Assign sections to rooms and instructors
        roomA101.assignSection(mathSection);
        roomB101.assignSection(physSection);
        labRoom.assignSection(labSection);
        drSmith.assignSection(mathSection);
        drJones.assignSection(physSection);
        drLab.assignSection(labSection);

        // Student enrolls in sections
        student1.enlist(mathSection);
        student1.enlist(physSection);
        student1.enlist(labSection);

        // Verify enrollment
        assertEquals(3, student1.getEnrolledSections().size());
        assertTrue(student1.getEnrolledSections().contains(mathSection));
        assertTrue(student1.getEnrolledSections().contains(physSection));
        assertTrue(student1.getEnrolledSections().contains(labSection));

        // Verify section enrollment counts
        assertEquals(1, mathSection.getEnrollmentCount());
        assertEquals(1, physSection.getEnrollmentCount());
        assertEquals(1, labSection.getEnrollmentCount());

        // Test assessment calculation
        BigDecimal assessment = student1.requestAssessment();
        // Expected: (3+4+1)*2345.67 + 1*1234.56 + 3456.78 + VAT
        // = 8*2345.67 + 1234.56 + 3456.78 + VAT
        // = 18765.36 + 1234.56 + 3456.78 + VAT
        // = 23456.70 + 2814.80 (12% VAT)
        // = 26271.50
        BigDecimal expected = new BigDecimal("26271.50");
        assertEquals(0, expected.compareTo(assessment));
    }

    @Test
    void testRoomCapacityConstraint() throws EnlistmentException {
        Room smallRoom = new Room("SMALL1", 1);
        Section section = new Section("MATH101A", math101, scheduleMTH830, smallRoom, drSmith);
        smallRoom.assignSection(section);
        drSmith.assignSection(section);

        // First student enrolls successfully
        student1.enlist(section);
        assertTrue(section.isAtCapacity());

        // Second student cannot enroll due to capacity
        assertThrows(CapacityReachedException.class, () -> student2.enlist(section));
    }

    @Test
    void testInstructorScheduleConflict() throws ScheduleConflictException {
        Schedule conflictingSchedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        
        // First section assigned to instructor
        Section section1 = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        roomA101.assignSection(section1);
        drSmith.assignSection(section1);

        // Attempt to create conflicting section with same instructor should fail
        assertThrows(ScheduleConflictException.class, () -> {
            Section section2 = new Section("PHYS101A", phys101, conflictingSchedule, roomB101, drSmith);
        });
    }

    @Test
    void testRoomScheduleConflict() throws ScheduleConflictException {
        Schedule conflictingSchedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        
        // First section assigned to room
        Section section1 = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        roomA101.assignSection(section1);
        drSmith.assignSection(section1);

        // Attempt to create conflicting section in same room should fail
        assertThrows(ScheduleConflictException.class, () -> {
            Section section2 = new Section("PHYS101A", phys101, conflictingSchedule, roomA101, drJones);
        });
    }

    @Test
    void testComplexPrerequisiteScenario() throws EnlistmentException {
        // Create prerequisite chain: MATH101 -> MATH201
        Section math101Section = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        Section math201Section = new Section("MATH201A", math201, scheduleTF830, roomB101, drJones);
        
        roomA101.assignSection(math101Section);
        roomB101.assignSection(math201Section);
        drSmith.assignSection(math101Section);
        drJones.assignSection(math201Section);

        // Student cannot enroll in MATH201 without completing MATH101
        assertThrows(PrerequisiteNotMetException.class, () -> student1.enlist(math201Section));

        // Student completes MATH101 prerequisite
        student1.completeSubject(math101);

        // Now student can enroll in MATH201
        student1.enlist(math201Section);
        assertTrue(student1.getEnrolledSections().contains(math201Section));
    }

    @Test
    void testCancelEnrollmentAndReenroll() throws EnlistmentException {
        Section mathSection = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        roomA101.assignSection(mathSection);
        drSmith.assignSection(mathSection);

        // Enroll and verify
        student1.enlist(mathSection);
        assertEquals(1, student1.getEnrolledSections().size());
        assertEquals(1, mathSection.getEnrollmentCount());

        // Cancel and verify
        student1.cancel(mathSection);
        assertEquals(0, student1.getEnrolledSections().size());
        assertEquals(0, mathSection.getEnrollmentCount());

        // Re-enroll and verify
        student1.enlist(mathSection);
        assertEquals(1, student1.getEnrolledSections().size());
        assertEquals(1, mathSection.getEnrollmentCount());
    }

    @Test
    void testMultipleStudentsInSameSection() throws EnlistmentException {
        Section mathSection = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
        roomA101.assignSection(mathSection);
        drSmith.assignSection(mathSection);

        // Both students enroll in same section
        student1.enlist(mathSection);
        student2.enlist(mathSection);

        assertEquals(2, mathSection.getEnrollmentCount());
        assertTrue(mathSection.getEnrolledStudents().contains(student1));
        assertTrue(mathSection.getEnrolledStudents().contains(student2));
    }

    @Test
    void testAssessmentWithMultipleLaboratorySubjects() throws EnlistmentException {
        Subject chem201L = new Subject("CHEM201L", 1, true);
        Subject phys101L = new Subject("PHYS101L", 1, true);
        
        Section labSection1 = new Section("CHEM101LA", chem101L, scheduleMTH830, labRoom, drLab);
        Section labSection2 = new Section("CHEM201LA", chem201L, scheduleTF830, new Room("LAB2", 15), drLab);
        Section labSection3 = new Section("PHYS101LA", phys101L, scheduleWS1000, new Room("LAB3", 15), new Instructor("Dr. Physics"));

        labRoom.assignSection(labSection1);
        drLab.assignSection(labSection1);
        drLab.assignSection(labSection2);

        student1.enlist(labSection1);
        student1.enlist(labSection2);
        student1.enlist(labSection3);

        BigDecimal assessment = student1.requestAssessment();
        
        // Expected: 3 units * 2345.67 + 3 lab fees * 1234.56 + misc fee + VAT
        // = 7037.01 + 3703.68 + 3456.78 + VAT
        // = 14197.47 + 1703.70 (12% VAT)
        // = 15901.17
        BigDecimal expected = new BigDecimal("15901.17");
        assertEquals(0, expected.compareTo(assessment));
    }
}