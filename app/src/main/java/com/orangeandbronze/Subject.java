package com.orangeandbronze;

import java.util.*;

class Subject {
    private final String subjectId;
    private final int units;
    private final boolean isLaboratory;
    private final Set<Subject> prerequisites;

    Subject(String subjectId, int units, boolean isLaboratory) {
        if (!ValidationUtils.isAlphanumeric(subjectId)) {
            throw new IllegalArgumentException("Subject ID must be alphanumeric");
        }
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be positive");
        }
        this.subjectId = subjectId;
        this.units = units;
        this.isLaboratory = isLaboratory;
        this.prerequisites = new HashSet<>();
    }

    void addPrerequisite(Subject prerequisite) {
        prerequisites.add(prerequisite);
    }

    // Getters
    String getSubjectId() {
        return subjectId;
    }

    int getUnits() {
        return units;
    }

    boolean isLaboratory() {
        return isLaboratory;
    }

    Set<Subject> getPrerequisites() {
        return new HashSet<>(prerequisites);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Subject subject = (Subject) obj;
        return Objects.equals(subjectId, subject.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }
}