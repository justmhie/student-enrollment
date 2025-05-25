package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
    
    @Test
    public void testValidScheduleCreation() {
        Schedule schedule = new Schedule("Mon/Thu", "8:30am-10am");
        assertEquals("Mon/Thu", schedule.getDays());
        assertEquals("8:30am-10am", schedule.getPeriod());
    }
    
    @Test
    public void testInvalidDaysThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule("Mon/Wed", "8:30am-10am");
        });
    }
    
    @Test
    public void testInvalidPeriodThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule("Mon/Thu", "8am-10am");
        });
    }
    
    @Test
    public void testNullArgumentsThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(null, "8:30am-10am");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule("Mon/Thu", null);
        });
    }
    
    @Test
    public void testScheduleConflicts() {
        Schedule schedule1 = new Schedule("Mon/Thu", "8:30am-10am");
        Schedule schedule2 = new Schedule("Mon/Thu", "8:30am-10am");
        Schedule schedule3 = new Schedule("Mon/Thu", "10am-11:30am");
        Schedule schedule4 = new Schedule("Tue/Fri", "8:30am-10am");
        
        assertTrue(schedule1.conflictsWith(schedule2));
        assertFalse(schedule1.conflictsWith(schedule3));
        assertFalse(schedule1.conflictsWith(schedule4));
    }
    
    @Test
    public void testScheduleEquality() {
        Schedule schedule1 = new Schedule("Wed/Sat", "1pm-2:30pm");
        Schedule schedule2 = new Schedule("Wed/Sat", "1pm-2:30pm");
        Schedule schedule3 = new Schedule("Wed/Sat", "2:30pm-4pm");
        
        assertEquals(schedule1, schedule2);
        assertNotEquals(schedule1, schedule3);
    }
}