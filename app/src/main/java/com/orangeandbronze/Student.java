package com.orangeandbronze;

import java.util.*;

public class Student {
    private final int studentNumber;
    private final Set<Section> enrolledSections;
    
    public Student(int studentNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Student number must be non-negative");
        }
        this.studentNumber = studentNumber;
        this.enrolledSections = new HashSet<>();
    }
    
    public int getStudentNumber() {
        return studentNumber;
    }
    
    public void enlist(Section section) {
        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null");
        }
        
        if (isEnrolledIn(section)) {
            throw new IllegalStateException("Student is already enrolled in section " + section.getSectionId());
        }
        
        if (!section.canAcceptMoreStudents()) {
            throw new IllegalStateException("Section " + section.getSectionId() + " is at full capacity");
        }
        
        for (Section enrolledSection : enrolledSections) {
            if (enrolledSection.hasScheduleConflictWith(section)) {
                throw new IllegalStateException("Schedule conflict with section " + enrolledSection.getSectionId());
            }
        }
        
        enrolledSections.add(section);
        section.addStudent(this);
    }
    
    public void cancel(Section section) {
        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null");
        }
        
        if (!isEnrolledIn(section)) {
            throw new IllegalStateException("Student is not enrolled in section " + section.getSectionId());
        }
        
        enrolledSections.remove(section);
        section.removeStudent(this);
    }
    
    public boolean isEnrolledIn(Section section) {
        return enrolledSections.contains(section);
    }
    
    public Set<Section> getEnrolledSections() {
        return new HashSet<>(enrolledSections);
    }
    
    public int getEnrolledSectionCount() {
        return enrolledSections.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentNumber == student.studentNumber;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(studentNumber);
    }
    
    @Override
    public String toString() {
        return "Student " + studentNumber;
    }
}