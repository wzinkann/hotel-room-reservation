package com.kayak.hotelsearch;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a hotel room booking request.
 * Contains guest information, room details, and booking status.
 * 
 * Interviewer Note:
 * This class is designed to be fully immutable. The status field is not updated via a setter.
 * Instead, a new BookingRequest instance is created for each status change. This approach ensures thread safety
 * and aligns with the principle of immutability, which is beneficial for concurrent systems.
 * While this adds some complexity, it eliminates potential race conditions and makes the code more predictable.
 */
public class BookingRequest {
    private final UUID requestId;
    private final String guestName;
    private final int roomNumber;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;
    private final BookingStatus status;

    /**
     * Creates a new booking request with unique identifier.
     * Initializes request status as PENDING.
     * 
     * @throws IllegalArgumentException if validation fails
     */
    public BookingRequest(String guestName, int roomNumber, LocalDateTime checkIn, LocalDateTime checkOut) {
        validateInput(guestName, roomNumber, checkIn, checkOut);
        
        this.requestId = UUID.randomUUID();
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = BookingStatus.PENDING;
    }

    /**
     * Validates booking request input parameters.
     * 
     * @throws IllegalArgumentException if any validation fails
     */
    private void validateInput(String guestName, int roomNumber, LocalDateTime checkIn, LocalDateTime checkOut) {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        
        if (roomNumber < 100 || roomNumber > 999) {
            throw new IllegalArgumentException("Room number must be between 100 and 999");
        }
        
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }
        
        if (checkIn.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
    }

    // ===== GETTER METHODS =====
    public UUID getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Returns a formatted string representation of the booking request.
     * Includes all relevant booking details and current status.
     */
    @Override
    public String toString() {
        return String.format("BookingRequest{id=%s, guest='%s', room=%d, checkIn=%s, checkOut=%s, status=%s}",
                requestId, guestName, roomNumber, checkIn, checkOut, status);
    }

    /**
     * Represents the possible states of a booking request.
     * PENDING: Initial state when request is created
     * CONFIRMED: Room has been successfully booked
     * REJECTED: Booking request could not be fulfilled
     */
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        REJECTED
    }
} 