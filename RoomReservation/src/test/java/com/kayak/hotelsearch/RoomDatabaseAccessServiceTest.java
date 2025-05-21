package com.kayak.hotelsearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomDatabaseAccessService.
 * Tests room booking, releasing, and concurrent operations.
 */
class RoomDatabaseAccessServiceTest {
    private RoomDatabaseAccessService service;

    @BeforeEach
    void setUp() {
        service = new RoomDatabaseAccessService();
        service.initializeRooms();
    }

    @Test
    void testBookRoom_Success() {
        // Test booking a room that exists and has availability
        assertTrue(service.bookRoom(101));
    }

    @Test
    void testBookRoom_NonExistentRoom() {
        // Test booking a room that doesn't exist
        assertFalse(service.bookRoom(999));
    }

    @Test
    void testBookRoom_ConcurrentBookings() throws InterruptedException {
        // Test concurrent bookings for the same room
        Thread t1 = new Thread(() -> assertTrue(service.bookRoom(101)));
        Thread t2 = new Thread(() -> assertTrue(service.bookRoom(101)));
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Verify room availability
        Room room = service.getRoom(101);
        assertNotNull(room);
        assertEquals(3, room.getAvailableRooms()); // 5 total - 2 booked
    }

    @Test
    void testReleaseRoom() {
        // Book a room first
        assertTrue(service.bookRoom(101));
        
        // Get initial availability
        Room room = service.getRoom(101);
        int initialAvailability = room.getAvailableRooms();
        
        // Release the room
        service.releaseRoom(101);
        
        // Verify availability increased
        assertEquals(initialAvailability + 1, room.getAvailableRooms());
    }

    @Test
    void testGetAllRooms() {
        // Verify all rooms are returned
        var rooms = service.getAllRooms();
        assertEquals(3, rooms.size());
        assertTrue(rooms.containsKey(101));
        assertTrue(rooms.containsKey(102));
        assertTrue(rooms.containsKey(103));
    }

    @Test
    void testClear() {
        // Verify rooms exist before clearing
        assertFalse(service.getAllRooms().isEmpty());
        
        // Clear the database
        service.clear();
        
        // Verify all rooms are removed
        assertTrue(service.getAllRooms().isEmpty());
    }
} 