package com.orangeandbronze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SectionTest {
    
    private Schedule schedule;
    private Room room;
    private Section section;
    
    @BeforeEach
    public void setUp() {
        schedule = new Schedule("Mon/Thu", "8:30am-10am");
        room = new Room("A101", 2);
        section = new Section("MATH101", schedule, room);
    }
    
    @Test
    public void testValidSectionCreation() {
        assertEquals("MATH101", section.getSectionId());
        assertEquals(schedule, section.getSchedule());
        assertEquals(room, section.getRoom());
        assertEquals(0, section.getEnrolledCount());
    }
    
    @Test
    public void testNullSectionIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Section(null, schedule, room);
        });
    }
    
    @Test
    public void testEmptySectionIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Section("", schedule, room);
        });
    }
    
    @Test
    public void testNullScheduleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Section("MATH101", null, room);
        });
    }
    
    @Test
    public void testNullRoomThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Section("MATH101", schedule, null);
        });
    }
    
    @Test
    public void testScheduleConflictDetection() {
        Schedule conflictingSchedule = new Schedule("Mon/Thu", "8:30am-10am");
        Schedule nonConflictingSchedule = new Schedule("Tue/Fri", "8:30am-10am");
        
        Section conflictingSection = new Section("PHYS101", conflictingSchedule, room);
        Section nonConflictingSection = new Section("CHEM101", nonConflictingSchedule, room);
        
        assertTrue(section.hasScheduleConflictWith(conflictingSection));
        assertFalse(section.hasScheduleConflictWith(nonConflictingSection));
    }
    
    @Test
    public void testCapacityCheck() {
        assertTrue(section.canAcceptMoreStudents());
        
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        
        section.addStudent(student1);
        assertTrue(section.canAcceptMoreStudents());
        assertEquals(1, section.getEnrolledCount());
        
        section.addStudent(student2);
        assertFalse(section.canAcceptMoreStudents());
        assertEquals(2, section.getEnrolledCount());
    }
    
    @Test
    public void testSectionEquality() {
        Section section1 = new Section("CS101", schedule, room);
        Section section2 = new Section("CS101", 
            new Schedule("Tue/Fri", "10am-11:30am"), 
            new Room("B202", 30));
        Section section3 = new Section("CS102", schedule, room);
        
        assertEquals(section1, section2); // Same ID
        assertNotEquals(section1, section3); // Different ID
    }
}