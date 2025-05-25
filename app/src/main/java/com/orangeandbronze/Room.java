package com.orangeandbronze;

import java.util.*;

public class Room {
    private final String roomName;
    private final int capacity;
    private final Set<Section> assignedSections;

    public Room(String roomName, int capacity) {
        if (!ValidationUtils.isAlphanumeric(roomName)) {
            throw new IllegalArgumentException("Room name must be alphanumeric");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.roomName = roomName;
        this.capacity = capacity;
        this.assignedSections = new HashSet<>();
    }

    public void assignSection(Section section) {
        assignedSections.add(section);
    }

    public void removeSection(Section section) {
        assignedSections.remove(section);
    }

    // Getters
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public Set<Section> getAssignedSections() { return new HashSet<>(assignedSections); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return Objects.equals(roomName, room.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName);
    }
}