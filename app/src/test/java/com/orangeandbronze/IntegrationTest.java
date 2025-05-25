package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    
    @Test
    public void testCompleteEnlistmentScenario() {
        // Create students
        Student alice = new Student(1001);
        Student bob = new Student(1002);
        Student charlie = new Student(1003);
        
        // Create rooms
        Room mathRoom = new Room("MATH-101", 25);
        Room physRoom = new Room("PHYS-201", 30);
        Room smallRoom = new Room("LAB-A", 2);
        
        // Create schedules
        Schedule morningMWF = new Schedule("Mon/Thu", "8:30am-10am");
        Schedule afternoonTTh = new Schedule("Tue/Fri", "1pm-2:30pm");
        Schedule conflictingMorning = new Schedule("Mon/Thu", "8:30am-10am");
        
        // Create sections
        Section mathSection = new Section("MATH-101-A", morningMWF, mathRoom);
        Section physSection = new Section("PHYS-201-B", afternoonTTh, physRoom);
        Section conflictSection = new Section("CHEM-101-C", conflictingMorning, smallRoom);
        Section smallSection = new Section("LAB-A-1", afternoonTTh, smallRoom);
        
        // Test successful enlistments
        alice.enlist(mathSection);
        alice.enlist(physSection);
        
        bob.enlist(mathSection);
        
        assertEquals(2, alice.getEnrolledSectionCount());
        assertEquals(1, bob.getEnrolledSectionCount());
        assertEquals(2, mathSection.getEnrolledCount());
        assertEquals(1, physSection.getEnrolledCount());
        
        // Test schedule conflict prevention
        assertThrows(IllegalStateException.class, () -> {
            alice.enlist(conflictSection);
        });
        
        // Test capacity limit
        charlie.enlist(smallSection);
        bob.enlist(smallSection);
        
        Student dave = new Student(1004);
        assertThrows(IllegalStateException.class, () -> {
            dave.enlist(smallSection); // Should fail - capacity exceeded
        });
        
        // Test cancellation and re-enrollment
        charlie.cancel(smallSection);
        assertDoesNotThrow(() -> {
            dave.enlist(smallSection);
        });
        
        // Test duplicate enrollment prevention
        assertThrows(IllegalStateException.class, () -> {
            alice.enlist(mathSection); // Already enrolled
        });
    }
    
    @Test
    public void testAllValidScheduleCombinations() {
        Student student = new Student(2001);
        Room bigRoom = new Room("AUDITORIUM", 100);
        
        String[] validDays = {"Mon/Thu", "Tue/Fri", "Wed/Sat"};
        String[] validPeriods = {
            "8:30am-10am", "10am-11:30am", "11:30am-1pm", 
            "1pm-2:30pm", "2:30pm-4pm", "4pm-5:30pm"
        };
        
        // Student should be able to enroll in one section per day combination
        // across different time periods
        for (int i = 0; i < validDays.length; i++) {
            Schedule schedule = new Schedule(validDays[i], validPeriods[i]);
            Section section = new Section("COURSE-" + i, schedule, bigRoom);
            
            assertDoesNotThrow(() -> {
                student.enlist(section);
            });
        }
        
        assertEquals(3, student.getEnrolledSectionCount());
        
        // But enrolling in a conflicting schedule should fail
        Schedule conflictingSchedule = new Schedule("Mon/Thu", "10am-11:30am");
        Section conflictingSection = new Section("CONFLICT", conflictingSchedule, bigRoom);
        
        assertThrows(IllegalStateException.class, () -> {
            student.enlist(conflictingSection);
        });
    }
}
