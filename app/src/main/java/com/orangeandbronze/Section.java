package com.orangeandbronze;

import java.util.*;
import com.orangeandbronze.exceptions.*;

class Section {
    private final String sectionId;
    private final Subject subject;
    private final Schedule schedule;
    private final Room room;
    private final Instructor instructor;
    private final Set<Student> enrolledStudents;

    Section(String sectionId, Subject subject, Schedule schedule, Room room, Instructor instructor)
            throws ScheduleConflictException {
        if (!ValidationUtils.isAlphanumeric(sectionId)) {
            throw new IllegalArgumentException("Section ID must be alphanumeric");
        }
        this.sectionId = sectionId;
        this.subject = Objects.requireNonNull(subject);
        this.schedule = Objects.requireNonNull(schedule);
        this.room = Objects.requireNonNull(room);
        this.instructor = Objects.requireNonNull(instructor);
        this.enrolledStudents = new HashSet<>();

        validateRoomScheduleConflict();
        validateInstructorScheduleConflict();
    }

    private void validateRoomScheduleConflict() throws ScheduleConflictException {
        for (Section section : room.getAssignedSections()) {
            if (section != this && section.hasScheduleConflict(this)) {
                throw new ScheduleConflictException("Room " + room.getRoomName() +
                        " has schedule conflict between sections");
            }
        }
    }

    private void validateInstructorScheduleConflict() throws ScheduleConflictException {
        for (Section section : instructor.getAssignedSections()) {
            if (section != this && section.hasScheduleConflict(this)) {
                throw new ScheduleConflictException("Instructor " + instructor.getName() +
                        " has schedule conflict between sections");
            }
        }
    }

    boolean hasScheduleConflict(Section other) {
        return this.schedule.conflictsWith(other.schedule);
    }

    boolean isAtCapacity() {
        return enrolledStudents.size() >= room.getCapacity();
    }

    void addStudent(Student student) throws EnlistmentException {
        if (isAtCapacity()) {
            throw new CapacityReachedException("Section is at full capacity");
        }
        enrolledStudents.add(student);
    }

    void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    // Getters
    String getSectionId() {
        return sectionId;
    }

    Subject getSubject() {
        return subject;
    }

    Schedule getSchedule() {
        return schedule;
    }

    Room getRoom() {
        return room;
    }

    Instructor getInstructor() {
        return instructor;
    }

    Set<Student> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents);
    }

    int getEnrollmentCount() {
        return enrolledStudents.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Section section = (Section) obj;
        return Objects.equals(sectionId, section.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId);
    }
}