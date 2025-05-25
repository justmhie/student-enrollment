package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void testScheduleCreation() {
        Schedule schedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        
        assertEquals(Schedule.Days.MTH, schedule.getDays());
        assertEquals(Schedule.Period.H0830_1000, schedule.getPeriod());
    }

    @Test
    void testScheduleConflict() {
        Schedule schedule1 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Schedule schedule2 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Schedule schedule3 = new Schedule(Schedule.Days.TF, Schedule.Period.H0830_1000);
        Schedule schedule4 = new Schedule(Schedule.Days.MTH, Schedule.Period.H1000_1130);
        
        assertTrue(schedule1.conflictsWith(schedule2));
        assertFalse(schedule1.conflictsWith(schedule3));
        assertFalse(schedule1.conflictsWith(schedule4));
    }

    @Test
    void testScheduleEquality() {
        Schedule schedule1 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Schedule schedule2 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
        Schedule schedule3 = new Schedule(Schedule.Days.TF, Schedule.Period.H0830_1000);
        
        assertEquals(schedule1, schedule2);
        assertNotEquals(schedule1, schedule3);
    }

    @Test
    void testPeriodTimeRange() {
        assertEquals("8:30am-10am", Schedule.Period.H0830_1000.getTimeRange());
        assertEquals("10am-11:30am", Schedule.Period.H1000_1130.getTimeRange());
    }
}