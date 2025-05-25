// EnrollmentSystemDemo.java
package com.orangeandbronze;

import java.math.BigDecimal;

import com.orangeandbronze.exceptions.EnlistmentException;
import com.orangeandbronze.exceptions.PrerequisiteNotMetException;
import com.orangeandbronze.exceptions.ScheduleConflictException;

/**
 * Demo class showing how to use the Student Enrollment System
 */
public class EnrollmentSystemDemo {
    
    public static void main(String[] args) {
        try {
            // Create students
            Student student1 = new Student(12345);
            Student student2 = new Student(67890);
            
            // Create subjects
            Subject math101 = new Subject("MATH101", 3, false);
            Subject math201 = new Subject("MATH201", 3, false);
            Subject chem101L = new Subject("CHEM101L", 1, true);
            Subject phys101 = new Subject("PHYS101", 4, false);
            
            // Set prerequisites
            math201.addPrerequisite(math101);
            
            // Create rooms
            Room roomA101 = new Room("A101", 30);
            Room roomB101 = new Room("B101", 25);
            Room labRoom = new Room("LAB1", 15);
            
            // Create instructors
            Instructor drSmith = new Instructor("Dr. Smith");
            Instructor drJones = new Instructor("Dr. Jones");
            Instructor drLab = new Instructor("Dr. Lab");
            
            // Create schedules
            Schedule scheduleMTH830 = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
            Schedule scheduleTF1000 = new Schedule(Schedule.Days.TF, Schedule.Period.H1000_1130);
            Schedule scheduleWS1130 = new Schedule(Schedule.Days.WS, Schedule.Period.H1130_1300);
            
            // Create sections
            Section mathSection = new Section("MATH101A", math101, scheduleMTH830, roomA101, drSmith);
            Section physSection = new Section("PHYS101A", phys101, scheduleTF1000, roomB101, drJones);
            Section labSection = new Section("CHEM101LA", chem101L, scheduleWS1130, labRoom, drLab);
            
            // Assign sections to rooms and instructors (for conflict checking)
            roomA101.assignSection(mathSection);
            roomB101.assignSection(physSection);
            labRoom.assignSection(labSection);
            drSmith.assignSection(mathSection);
            drJones.assignSection(physSection);
            drLab.assignSection(labSection);
            
            System.out.println("=== Student Enrollment System Demo ===\n");
            
            // Demonstrate successful enrollment
            System.out.println("1. Enrolling Student " + student1.getStudentNumber() + " in sections...");
            student1.enlist(mathSection);
            System.out.println("   ✓ Enrolled in " + mathSection.getSectionId() + " (" + mathSection.getSubject().getSubjectId() + ")");
            
            student1.enlist(physSection);
            System.out.println("   ✓ Enrolled in " + physSection.getSectionId() + " (" + physSection.getSubject().getSubjectId() + ")");
            
            student1.enlist(labSection);
            System.out.println("   ✓ Enrolled in " + labSection.getSectionId() + " (" + labSection.getSubject().getSubjectId() + ")");
            
            // Show enrollment summary
            System.out.println("\n2. Enrollment Summary for Student " + student1.getStudentNumber() + ":");
            for (Section section : student1.getEnrolledSections()) {
                System.out.println("   - " + section.getSectionId() + ": " + 
                    section.getSubject().getSubjectId() + " (" + 
                    section.getSubject().getUnits() + " units" + 
                    (section.getSubject().isLaboratory() ? ", Laboratory" : "") + ")");
                System.out.println("     Schedule: " + section.getSchedule());
                System.out.println("     Room: " + section.getRoom().getRoomName());
                System.out.println("     Instructor: " + section.getInstructor().getName());
            }
            
            // Calculate and display assessment
            System.out.println("\n3. Assessment Calculation:");
            BigDecimal assessment = student1.requestAssessment();
            System.out.println("   Total Amount Due: ₱" + assessment);
            
            // Demonstrate error cases
            System.out.println("\n4. Demonstrating Error Cases:");
            
            // Try to enroll in same section again
            try {
                student1.enlist(mathSection);
            } catch (EnlistmentException e) {
                System.out.println("   ✗ Duplicate enrollment prevented: " + e.getMessage());
            }
            
            // Try to enroll second student to show schedule conflict
            try {
                Schedule conflictingSchedule = new Schedule(Schedule.Days.MTH, Schedule.Period.H0830_1000);
                Section conflictingSection = new Section("PHYS101B", phys101, conflictingSchedule, roomB101, drJones);
                student1.enlist(conflictingSection);
            } catch (ScheduleConflictException e) {
                System.out.println("   ✗ Schedule conflict prevented: " + e.getMessage());
            }
            
            // Try to enroll in subject with unmet prerequisite
            try {
                Section advancedMathSection = new Section("MATH201A", math201, scheduleTF1000, roomB101, drJones);
                Student newStudent = new Student(99999);
                newStudent.enlist(advancedMathSection);
            } catch (PrerequisiteNotMetException e) {
                System.out.println("   ✗ Prerequisite check prevented enrollment: " + e.getMessage());
            }
            
            // Demonstrate cancellation
            System.out.println("\n5. Demonstrating Cancellation:");
            System.out.println("   Enrolled sections before cancellation: " + student1.getEnrolledSections().size());
            student1.cancel(labSection);
            System.out.println("   Enrolled sections after cancelling lab: " + student1.getEnrolledSections().size());
            
            // Show updated assessment
            BigDecimal newAssessment = student1.requestAssessment();
            System.out.println("   Updated assessment: ₱" + newAssessment);
            
            System.out.println("\n=== Demo Complete ===");
            
        } catch (Exception e) {
            System.err.println("An error occurred during the demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}