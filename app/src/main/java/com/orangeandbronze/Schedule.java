package com.orangeandbronze;

import java.util.Objects;

class Schedule {
    enum Days {
        MTH, // Mon/Thu
        TF, // Tue/Fri
        WS // Wed/Sat
    }

    enum Period {
        H0830_1000("8:30am-10am"),
        H1000_1130("10am-11:30am"),
        H1130_1300("11:30am-1pm"),
        H1300_1430("1pm-2:30pm"),
        H1430_1600("2:30pm-4pm"),
        H1600_1730("4pm-5:30pm");

        private final String timeRange;

        Period(String timeRange) {
            this.timeRange = timeRange;
        }

        String getTimeRange() {
            return timeRange;
        }
    }

    private final Days days;
    private final Period period;

    Schedule(Days days, Period period) {
        this.days = Objects.requireNonNull(days);
        this.period = Objects.requireNonNull(period);
    }

    boolean conflictsWith(Schedule other) {
        return this.days == other.days && this.period == other.period;
    }

    // Getters
    Days getDays() {
        return days;
    }

    Period getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Schedule schedule = (Schedule) obj;
        return days == schedule.days && period == schedule.period;
    }

    @Override
    public int hashCode() {
        return Objects.hash(days, period);
    }

    @Override
    public String toString() {
        return days + " " + period.getTimeRange();
    }
}