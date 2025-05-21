package com.kayak.hotelsearch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service layer for room database operations.
 * Provides thread-safe access to room data using concurrent collections.
 */
public class RoomDatabaseAccessService {
    private static final Logger logger = LoggerFactory.getLogger(RoomDatabaseAccessService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 100;

    private final Map<Integer, Room> rooms;
    private final ReadWriteLock lock;

    /**
     * Initializes the room database with thread-safe collections.
     * Uses ConcurrentHashMap for room storage and ReadWriteLock for synchronization.
     */
    public RoomDatabaseAccessService() {
        this.rooms = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Initializes the room database with sample data.
     * Creates rooms of different types with varying availability.
     */
    public void initializeRooms() {
        // Initialize with 10 rooms of different types
        addRoom(new Room(101, "Standard", 5));
        addRoom(new Room(102, "Deluxe", 3));
        addRoom(new Room(103, "Suite", 2));
    }

    /**
     * Adds a new room to the database.
     * Thread-safe operation using ConcurrentHashMap.
     */
    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    /**
     * Retrieves a room by its number.
     * Returns null if room doesn't exist.
     */
    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    /**
     * Attempts to book a room by its number with retry mechanism.
     * Returns true if booking was successful, false otherwise.
     */
    public boolean bookRoom(int roomNumber) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                Room room = rooms.get(roomNumber);
                if (room == null) {
                    logger.warn("Room {} not found", roomNumber);
                    return false;
                }
                
                boolean booked = room.bookRoom();
                if (booked) {
                    logger.info("Successfully booked room {}", roomNumber);
                    return true;
                }
                
                retries++;
                if (retries < MAX_RETRIES) {
                    logger.info("Retrying booking for room {} (attempt {}/{})", roomNumber, retries + 1, MAX_RETRIES);
                    Thread.sleep(RETRY_DELAY_MS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Booking interrupted for room {}", roomNumber);
                return false;
            } catch (Exception e) {
                logger.error("Error booking room {}: {}", roomNumber, e.getMessage());
                return false;
            }
        }
        
        logger.warn("Failed to book room {} after {} attempts", roomNumber, MAX_RETRIES);
        return false;
    }

    /**
     * Releases a previously booked room.
     * No-op if room doesn't exist.
     */
    public void releaseRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            room.releaseRoom();
            logger.info("Released room {}", roomNumber);
        } else {
            logger.warn("Attempted to release non-existent room {}", roomNumber);
        }
    }

    /**
     * Returns a thread-safe copy of all rooms.
     * Uses ConcurrentHashMap for thread safety.
     */
    public Map<Integer, Room> getAllRooms() {
        return new ConcurrentHashMap<>(rooms);
    }

    /**
     * Clears all rooms from the database.
     * Use with caution in production environment.
     */
    public void clear() {
        rooms.clear();
        logger.info("Cleared all rooms from database");
    }
} 