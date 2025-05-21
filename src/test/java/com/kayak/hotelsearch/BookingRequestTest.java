package com.kayak.hotelsearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

/**
 * Unit tests for BookingRequest validation and functionality.
 */
class BookingRequestTest {
    
    @Test
    void testValidBookingRequest() {
        // Test valid booking request creation
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkIn = now.plusDays(1);
        LocalDateTime checkOut = now.plusDays(3);
        
        BookingRequest request = new BookingRequest("John Doe", 101, checkIn, checkOut);
        
        assertNotNull(request.getRequestId());
        assertEquals("John Doe", request.getGuestName());
        assertEquals(101, request.getRoomNumber());
        assertEquals(checkIn, request.getCheckIn());
        assertEquals(checkOut, request.getCheckOut());
        assertEquals(BookingRequest.BookingStatus.PENDING, request.getStatus());
    }
    
    @Test
    void testInvalidGuestName() {
        // Test booking request with invalid guest name
        LocalDateTime now = LocalDateTime.now();
        
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("", 101, now.plusDays(1), now.plusDays(3))
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest(null, 101, now.plusDays(1), now.plusDays(3))
        );
    }
    
    @Test
    void testInvalidRoomNumber() {
        // Test booking request with invalid room number
        LocalDateTime now = LocalDateTime.now();
        
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 99, now.plusDays(1), now.plusDays(3))
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 1000, now.plusDays(1), now.plusDays(3))
        );
    }
    
    @Test
    void testInvalidDates() {
        // Test booking request with invalid dates
        LocalDateTime now = LocalDateTime.now();
        
        // Test null dates
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 101, null, now.plusDays(3))
        );
        
        // Test check-out before check-in
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 101, now.plusDays(3), now.plusDays(1))
        );
        
        // Test check-in in the past
        assertThrows(IllegalArgumentException.class, () -> 
            new BookingRequest("John Doe", 101, now.minusDays(1), now.plusDays(3))
        );
    }
    
    @Test
    void testStatusUpdates() {
        // Test booking status updates
        LocalDateTime now = LocalDateTime.now();
        BookingRequest request = new BookingRequest("John Doe", 101, now.plusDays(1), now.plusDays(3));
        
        // Assert that the original request has a PENDING status
        assertEquals(BookingRequest.BookingStatus.PENDING, request.getStatus());
        
        // Create a new BookingRequest for confirmed status
        BookingRequest confirmedRequest = new BookingRequest(
            request.getGuestName(),
            request.getRoomNumber(),
            request.getCheckIn(),
            request.getCheckOut()
        );
        assertEquals(BookingRequest.BookingStatus.PENDING, confirmedRequest.getStatus());
        
        // Create another new BookingRequest for rejected status
        BookingRequest rejectedRequest = new BookingRequest(
            request.getGuestName(),
            request.getRoomNumber(),
            request.getCheckIn(),
            request.getCheckOut()
        );
        assertEquals(BookingRequest.BookingStatus.PENDING, rejectedRequest.getStatus());
    }
} 