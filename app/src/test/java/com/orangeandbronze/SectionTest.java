package com.orangeandbronze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.orangeandbronze.exceptions.*;

class SectionTest {
    private Subject subject;
    private Schedule schedule;
    private Room room;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        subject = new Subject("MATH101", 3, false);
        schedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        room = new Room("A101", 30);
        instructor = new Instructor("Dr. Smith");
    }

    @Test
    void testSectionCreation() throws ScheduleConflictException {
        Section section = new Section("SEC001", subject, schedule, room, instructor);
        
        assertEquals("SEC001", section.getSectionId());
        assertEquals(subject, section.getSubject());
        assertEquals(schedule, section.getSchedule());
        assertEquals(room, section.getRoom());
        assertEquals(instructor, section.getInstructor());
        assertEquals(0, section.getEnrollmentCount());
    }

    @Test
    void testInvalidSectionId() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Section("SEC-001", subject, schedule, room, instructor));
    }

    @Test
    void testScheduleConflictDetection() throws ScheduleConflictException {
        Schedule sameSchedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Schedule differentSchedule = new Schedule(Schedule.Days.TF, Schedule.Period.H0830_1000);
        
        Section section1 = new Section("SEC001", subject, schedule, room, instructor);
        Section section2 = new Section("SEC002", subject, sameSchedule, new Room("B101", 25), new Instructor("Dr. Jones"));
        Section section3 = new Section("SEC003", subject, differentSchedule, new Room("C101", 25), new Instructor("Dr. Brown"));
        
        assertTrue(section1.hasScheduleConflict(section2));
        assertFalse(section1.hasScheduleConflict(section3));
    }

    @Test
    void testCapacityCheck() throws EnlistmentException {
        Room smallRoom = new Room("SMALL1", 2);
        Section section = new Section("SEC001", subject, schedule, smallRoom, instructor);
        smallRoom.assignSection(section);
        instructor.assignSection(section);
        
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        
        section.addStudent(student1);
        section.addStudent(student2);
        
        assertTrue(section.isAtCapacity());
        
        Student student3 = new Student(3);
        assertThrows(CapacityReachedException.class, () -> section.addStudent(student3));
    }
}