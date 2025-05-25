package com.orangeandbronze;

import org.junit.jupiter.api.Test;

import com.orangeandbronze.exceptions.ScheduleConflictException;

import static org.junit.jupiter.api.Assertions.*;

class InstructorTest {

    @Test
    void testInstructorCreation() {
        Instructor instructor = new Instructor("Dr. Smith");
        
        assertEquals("Dr. Smith", instructor.getName());
        assertTrue(instructor.getAssignedSections().isEmpty());
    }

    @Test
    void testNullName() {
        assertThrows(NullPointerException.class, () -> new Instructor(null));
    }

    @Test
    void testSectionAssignment() throws ScheduleConflictException {
        Instructor instructor = new Instructor("Dr. Smith");
        Subject subject = new Subject("MATH101", 3, false);
        Schedule schedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Room room = new Room("A101", 30);
        
        Section section = new Section("SEC001", subject, schedule, room, instructor);
        instructor.assignSection(section);
        
        assertTrue(instructor.getAssignedSections().contains(section));
        assertEquals(1, instructor.getAssignedSections().size());
    }
}