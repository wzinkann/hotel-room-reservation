package com.kayak.hotelsearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.kayak.hotelsearch.metrics.BookingMetrics;

/**
 * Integration tests for the complete booking system.
 * Tests the interaction between all components.
 */
class BookingSystemIntegrationTest {
    private RoomDatabaseAccessService roomService;
    private BookingMetrics metrics;
    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        roomService = new RoomDatabaseAccessService();
        roomService.initializeRooms();
        metrics = new BookingMetrics();
        executor = Executors.newFixedThreadPool(5);
    }

    @Test
    void testCompleteBookingFlow() {
        // Create a valid booking request
        LocalDateTime now = LocalDateTime.now();
        BookingRequest request = new BookingRequest(
            "John Doe",
            101,
            now.plusDays(1),
            now.plusDays(3)
        );

        // Attempt to book the room
        boolean booked = roomService.bookRoom(request.getRoomNumber());
        if (booked) {
            metrics.recordSuccessfulBooking();
        } else {
            metrics.recordFailedBooking();
        }
        assertTrue(booked);
        
        // Verify room availability decreased
        Room room = roomService.getRoom(101);
        assertEquals(4, room.getAvailableRooms()); // 5 total - 1 booked

        // Verify metrics
        assertEquals(1.0, metrics.getSuccessRate());
    }

    @Test
    void testConcurrentBookingRequests() throws InterruptedException {
        int numberOfRequests = 10;
        CountDownLatch latch = new CountDownLatch(numberOfRequests);
        LocalDateTime now = LocalDateTime.now();

        // Create multiple booking requests for the same room
        for (int i = 0; i < numberOfRequests; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    BookingRequest request = new BookingRequest(
                        "Guest " + index,
                        101,
                        now.plusDays(index),
                        now.plusDays(index + 2)
                    );
                    
                    boolean booked = roomService.bookRoom(request.getRoomNumber());
                    // Create a new BookingRequest with updated status instead of using setStatus
                    BookingRequest updatedRequest = new BookingRequest(
                        request.getGuestName(),
                        request.getRoomNumber(),
                        request.getCheckIn(),
                        request.getCheckOut()
                    );
                    // Note: The status is now immutable, so we don't update it here
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all requests to complete
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        // Verify room availability
        Room room = roomService.getRoom(101);
        assertNotNull(room);
        assertTrue(room.getAvailableRooms() >= 0);
        assertTrue(room.getAvailableRooms() <= 5);
    }

    @Test
    void testBookingAndReleasing() {
        // Book a room
        assertTrue(roomService.bookRoom(101));
        Room room = roomService.getRoom(101);
        int initialAvailability = room.getAvailableRooms();

        // Release the room
        roomService.releaseRoom(101);
        assertEquals(initialAvailability + 1, room.getAvailableRooms());

        // Try to book again
        assertTrue(roomService.bookRoom(101));
    }

    @Test
    void testInvalidBookingRequests() {
        LocalDateTime now = LocalDateTime.now();

        // Test booking non-existent room
        assertFalse(roomService.bookRoom(999));

        // Test booking with invalid dates
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 101, now.minusDays(1), now.plusDays(1))
        );

        // Test booking with invalid room number
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 99, now.plusDays(1), now.plusDays(3))
        );
    }

    @Test
    void testMetricsCollection() {
        // Make some booking attempts
        boolean b1 = roomService.bookRoom(101); // Should succeed
        if (b1) metrics.recordSuccessfulBooking(); else metrics.recordFailedBooking();
        boolean b2 = roomService.bookRoom(101); // Should succeed
        if (b2) metrics.recordSuccessfulBooking(); else metrics.recordFailedBooking();
        boolean b3 = roomService.bookRoom(999); // Should fail
        if (b3) metrics.recordSuccessfulBooking(); else metrics.recordFailedBooking();

        // Verify metrics
        assertTrue(metrics.getSuccessRate() > 0);
        assertTrue(metrics.getAverageProcessingTime() >= 0);
        assertTrue(metrics.getConcurrentBookingCount() >= 0);
    }
} 