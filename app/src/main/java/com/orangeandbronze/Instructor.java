package com.orangeandbronze;

import java.util.*;

class Instructor {
    private final String name;
    private final Set<Section> assignedSections;

    Instructor(String name) {
        this.name = Objects.requireNonNull(name);
        this.assignedSections = new HashSet<>();
    }

    void assignSection(Section section) {
        assignedSections.add(section);
    }

    void removeSection(Section section) {
        assignedSections.remove(section);
    }

    // Getters
    String getName() {
        return name;
    }

    Set<Section> getAssignedSections() {
        return new HashSet<>(assignedSections);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Instructor instructor = (Instructor) obj;
        return Objects.equals(name, instructor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}