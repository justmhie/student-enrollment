package com.orangeandbronze;

public class Schedule {
    private final String days;
    private final String period;
    
    public Schedule(String days, String period) {
        if (days == null || period == null) {
            throw new IllegalArgumentException("Days and period cannot be null");
        }
        validateDays(days);
        validatePeriod(period);
        this.days = days;
        this.period = period;
    }
    
    private void validateDays(String days) {
        String[] validDays = {"Mon/Thu", "Tue/Fri", "Wed/Sat"};
        for (String validDay : validDays) {
            if (validDay.equals(days)) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid days: " + days);
    }
    
    private void validatePeriod(String period) {
        String[] validPeriods = {
            "8:30am-10am", "10am-11:30am", "11:30am-1pm", 
            "1pm-2:30pm", "2:30pm-4pm", "4pm-5:30pm"
        };
        for (String validPeriod : validPeriods) {
            if (validPeriod.equals(period)) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid period: " + period);
    }
    
    public boolean conflictsWith(Schedule other) {
        return this.days.equals(other.days) && this.period.equals(other.period);
    }
    
    public String getDays() {
        return days;
    }
    
    public String getPeriod() {
        return period;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Schedule schedule = (Schedule) obj;
        return days.equals(schedule.days) && period.equals(schedule.period);
    }
    
    @Override
    public int hashCode() {
        return days.hashCode() + period.hashCode();
    }
    
    @Override
    public String toString() {
        return days + " " + period;
    }
}