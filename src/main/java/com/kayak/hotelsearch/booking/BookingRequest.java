package com.kayak.hotelsearch.booking;

/**
 * Represents a booking request for a hotel room.
 * This class is designed to be fully immutable, meaning its state cannot be changed after creation.
 * 
 * Pros of Immutability:
 * - Thread Safety: Immutable objects are inherently thread-safe, eliminating the need for synchronization.
 * - Predictability: The state of a BookingRequest cannot be altered, making the code easier to reason about.
 * - Simplicity: Immutable objects are simpler to use and test, as they have no side effects.
 * 
 * Cons of Immutability:
 * - Memory Overhead: Creating new objects for every state change can lead to increased memory usage.
 * - Performance: Frequent creation of new objects may impact performance in high-throughput scenarios.
 */
public class BookingRequest {
    private final int roomNumber;
    private final String guest;

    public BookingRequest(int roomNumber, String guest) {
        this.roomNumber = roomNumber;
        this.guest = guest;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getGuest() {
        return guest;
    }
}
