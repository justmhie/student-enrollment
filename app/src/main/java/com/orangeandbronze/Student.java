package com.orangeandbronze;

import java.math.BigDecimal;
import java.util.*;

import com.orangeandbronze.exceptions.CapacityReachedException;
import com.orangeandbronze.exceptions.EnlistmentException;
import com.orangeandbronze.exceptions.PrerequisiteNotMetException;
import com.orangeandbronze.exceptions.SameSubjectEnrollmentException;
import com.orangeandbronze.exceptions.ScheduleConflictException;

public class Student {
    private final int studentNumber;
    private final Set<Section> enrolledSections;
    private final Set<Subject> completedSubjects;

    public Student(int studentNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Student number must be non-negative");
        }
        this.studentNumber = studentNumber;
        this.enrolledSections = new HashSet<>();
        this.completedSubjects = new HashSet<>();
    }

    public void enlist(Section section) throws EnlistmentException {
        validateEnlistment(section);
        enrolledSections.add(section);
        section.addStudent(this);
    }

    private void validateEnlistment(Section section) throws EnlistmentException {
        // Check if already enrolled in this section
        if (enrolledSections.contains(section)) {
            throw new EnlistmentException("Student already enrolled in section " + section.getSectionId());
        }

        // Check for schedule conflicts
        for (Section enrolledSection : enrolledSections) {
            if (enrolledSection.hasScheduleConflict(section)) {
                throw new ScheduleConflictException("Schedule conflict between sections " + 
                    enrolledSection.getSectionId() + " and " + section.getSectionId());
            }
        }

        // Check for same subject enrollment
        for (Section enrolledSection : enrolledSections) {
            if (enrolledSection.getSubject().equals(section.getSubject())) {
                throw new SameSubjectEnrollmentException("Student already enrolled in subject " + 
                    section.getSubject().getSubjectId());
            }
        }

        // Check prerequisites
        Subject subject = section.getSubject();
        for (Subject prerequisite : subject.getPrerequisites()) {
            if (!completedSubjects.contains(prerequisite)) {
                throw new PrerequisiteNotMetException("Prerequisite " + prerequisite.getSubjectId() + 
                    " not completed for subject " + subject.getSubjectId());
            }
        }

        // Check room capacity
        if (section.isAtCapacity()) {
            throw new CapacityReachedException("Section " + section.getSectionId() + " is at full capacity");
        }
    }

    public void cancel(Section section) throws EnlistmentException {
        if (!enrolledSections.contains(section)) {
            throw new EnlistmentException("Student not enrolled in section " + section.getSectionId());
        }
        enrolledSections.remove(section);
        section.removeStudent(this);
    }

    public BigDecimal requestAssessment() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalUnits = 0;
        int labSubjectCount = 0;

        for (Section section : enrolledSections) {
            Subject subject = section.getSubject();
            totalUnits += subject.getUnits();
            if (subject.isLaboratory()) {
                labSubjectCount++;
            }
        }

        // Calculate base fee
        BigDecimal unitFee = new BigDecimal("2345.67");
        totalAmount = totalAmount.add(unitFee.multiply(new BigDecimal(totalUnits)));

        // Add laboratory fees
        BigDecimal labFee = new BigDecimal("1234.56");
        totalAmount = totalAmount.add(labFee.multiply(new BigDecimal(labSubjectCount)));

        // Add miscellaneous fees
        BigDecimal miscFee = new BigDecimal("3456.78");
        totalAmount = totalAmount.add(miscFee);

        // Add VAT (12%)
        BigDecimal vat = totalAmount.multiply(new BigDecimal("0.12"));
        totalAmount = totalAmount.add(vat);

        return totalAmount;
    }

    public void completeSubject(Subject subject) {
        completedSubjects.add(subject);
    }

    // Getters
    public int getStudentNumber() { return studentNumber; }
    public Set<Section> getEnrolledSections() { return new HashSet<>(enrolledSections); }
    public Set<Subject> getCompletedSubjects() { return new HashSet<>(completedSubjects); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentNumber == student.studentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }
}