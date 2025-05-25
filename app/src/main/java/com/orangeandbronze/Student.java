package com.orangeandbronze;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.orangeandbronze.exceptions.*;

 class Student {
    private final int studentNumber;
    private final Set<Section> enrolledSections;
    private final Set<Subject> completedSubjects;

     Student(int studentNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Student number must be non-negative");
        }
        this.studentNumber = studentNumber;
        this.enrolledSections = new HashSet<>();
        this.completedSubjects = new HashSet<>();
    }

     void enlist(Section section) throws EnlistmentException {
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

     void cancel(Section section) throws EnlistmentException {
        if (!enrolledSections.contains(section)) {
            throw new EnlistmentException("Student not enrolled in section " + section.getSectionId());
        }
        enrolledSections.remove(section);
        section.removeStudent(this);
    }

     BigDecimal requestAssessment() {
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

        BigDecimal unitFee = new BigDecimal("2345.67");
        BigDecimal labFee = new BigDecimal("1234.56");
        BigDecimal miscFee = new BigDecimal("3456.78");

        // Calculate base
        totalAmount = totalAmount
                .add(unitFee.multiply(new BigDecimal(totalUnits)))
                .add(labFee.multiply(new BigDecimal(labSubjectCount)))
                .add(miscFee);

        // Calculate VAT
        BigDecimal vat = totalAmount.multiply(new BigDecimal("0.12"));
        vat = vat.setScale(2, RoundingMode.HALF_UP); // <-- ROUND IT!

        totalAmount = totalAmount.add(vat);

        return totalAmount.setScale(2, RoundingMode.HALF_UP); // <-- ROUND FINAL TOTAL
    }

     void completeSubject(Subject subject) {
        completedSubjects.add(subject);
    }

    // Getters
     int getStudentNumber() {
        return studentNumber;
    }

     Set<Section> getEnrolledSections() {
        return new HashSet<>(enrolledSections);
    }

     Set<Subject> getCompletedSubjects() {
        return new HashSet<>(completedSubjects);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return studentNumber == student.studentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }
}