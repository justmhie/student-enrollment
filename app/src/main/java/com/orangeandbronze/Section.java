package com.orangeandbronze;

import java.util.*;

public class Section {
    private final String sectionId;
    private final Schedule schedule;
    private final Room room;
    private final Set<Student> enrolledStudents;
    
    public Section(String sectionId, Schedule schedule, Room room) {
        if (sectionId == null || sectionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Section ID cannot be null or empty");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
        this.enrolledStudents = new HashSet<>();
    }
    
    public String getSectionId() {
        return sectionId;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public boolean hasScheduleConflictWith(Section other) {
        return this.schedule.conflictsWith(other.schedule);
    }
    
    public boolean canAcceptMoreStudents() {
        return enrolledStudents.size() < room.getCapacity();
    }
    
    public int getEnrolledCount() {
        return enrolledStudents.size();
    }
    
    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }
    
    void addStudent(Student student) {
        enrolledStudents.add(student);
    }
    
    void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Section section = (Section) obj;
        return sectionId.equals(section.sectionId);
    }
    
    @Override
    public int hashCode() {
        return sectionId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Section " + sectionId + " (" + schedule + ") in " + room;
    }
}