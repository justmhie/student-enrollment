package com.orangeandbronze;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testRoomCreation() {
        Room room = new Room("A101", 30);
        
        assertEquals("A101", room.getRoomName());
        assertEquals(30, room.getCapacity());
        assertTrue(room.getAssignedSections().isEmpty());
    }

    @Test
    void testInvalidRoomName() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Room("A-101", 30));
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Room("A101", 0));
        assertThrows(IllegalArgumentException.class, 
            () -> new Room("A101", -1));
    }
}
