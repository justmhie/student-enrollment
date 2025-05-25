package com.orangeandbronze;

import java.util.*;

public class Instructor {
    private final String name;
    private final Set<Section> assignedSections;

    public Instructor(String name) {
        this.name = Objects.requireNonNull(name);
        this.assignedSections = new HashSet<>();
    }

    public void assignSection(Section section) {
        assignedSections.add(section);
    }

    public void removeSection(Section section) {
        assignedSections.remove(section);
    }

    // Getters
    public String getName() { return name; }
    public Set<Section> getAssignedSections() { return new HashSet<>(assignedSections); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Instructor instructor = (Instructor) obj;
        return Objects.equals(name, instructor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}