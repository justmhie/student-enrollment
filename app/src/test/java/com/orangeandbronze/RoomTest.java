package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    
    @Test
    public void testValidRoomCreation() {
        Room room = new Room("A101", 30);
        assertEquals("A101", room.getName());
        assertEquals(30, room.getCapacity());
    }
    
    @Test
    public void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room(null, 30);
        });
    }
    
    @Test
    public void testEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("", 30);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("   ", 30);
        });
    }
    
    @Test
    public void testNegativeCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("A101", -1);
        });
    }
    
    @Test
    public void testZeroCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room("A101", 0);
        });
    }
    
    @Test
    public void testRoomEquality() {
        Room room1 = new Room("B202", 25);
        Room room2 = new Room("B202", 30);
        Room room3 = new Room("B203", 25);
        
        assertEquals(room1, room2); // Same name, different capacity
        assertNotEquals(room1, room3); // Different name
    }
}
