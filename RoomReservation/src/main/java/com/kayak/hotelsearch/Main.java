package com.kayak.hotelsearch;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayak.hotelsearch.booking.BookingRequest;
import com.kayak.hotelsearch.metrics.BookingMetrics;
import com.kayak.hotelsearch.Room;
import com.kayak.hotelsearch.RoomDatabaseAccessService;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application entry point for the Room Booking System.
 * Handles concurrent booking requests and room management.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final List<BookingRequest> incomingRequests = new ArrayList<>();
    private static final BookingMetrics metrics = new BookingMetrics();
    private static final RoomDatabaseAccessService roomService = new RoomDatabaseAccessService();

    public static void main(String[] args) {
        // ===== SECTION 1: PERSONAL INFORMATION =====
        logger.error("William Zinkann");
        logger.info("Yes, I prefer working from the office because it helps me stay focused, collaborate more effectively with my team, and maintain a clear boundary between work and personal life.");

        // ===== SECTION 2: ROOM MANAGEMENT INITIALIZATION =====
        roomService.initializeRooms();
        logger.info("Room database initialized with sample data");

        // ===== SECTION 3: BOOKING REQUEST PROCESSING =====
        List<BookingRequest> requests = createBookingRequests();
        processBookings(roomService, requests);

        // ===== SECTION 4: METRICS REPORTING =====
        reportMetrics();
    }

    /**
     * Generates a list of sample booking requests for testing purposes.
     * Creates 10 requests with rotating room numbers and sequential dates.
     */
    private static List<BookingRequest> createBookingRequests() {
        List<BookingRequest> requests = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            requests.add(new BookingRequest(
                101 + (i % 3), // Room numbers cycle: 101 (Standard), 102 (Deluxe), 103 (Suite)
                "Guest " + (i + 1)
            ));
        }

        logger.info("Created {} booking requests", requests.size());
        return requests;
    }

    /**
     * Processes booking requests concurrently using a thread pool.
     * Handles booking confirmations and rejections with proper error handling.
     */
    private static void processBookings(RoomDatabaseAccessService roomService, List<BookingRequest> requests) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        logger.info("Starting booking processing with thread pool size: 5");

        for (BookingRequest request : requests) {
            executor.submit(() -> {
                Timer.Sample timer = metrics.startBookingTimer();
                metrics.recordConcurrentBooking();

                try {
                    boolean booked = roomService.bookRoom(request.getRoomNumber());
                    if (booked) {
                        metrics.recordSuccessfulBooking();
                        logger.info("Successfully processed booking: Room {} for {}", 
                            request.getRoomNumber(), request.getGuest());
                    } else {
                        metrics.recordFailedBooking();
                        logger.warn("Failed to process booking: Room {} for {}", 
                            request.getRoomNumber(), request.getGuest());
                    }
                } catch (Exception e) {
                    metrics.recordFailedBooking();
                    logger.error("Error processing booking: Room {} for {} - {}", 
                        request.getRoomNumber(), request.getGuest(), e.getMessage());
                } finally {
                    metrics.stopBookingTimer(timer);
                }
            });
        }

        // Graceful shutdown with timeout
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warn("Forcing thread pool shutdown after timeout");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Thread pool interrupted during shutdown", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Display final room availability status
        logger.info("Final Room Status:");
        roomService.getAllRooms().values().forEach(room -> 
            logger.info("{}", room));
    }

    /**
     * Reports booking metrics to the console.
     */
    private static void reportMetrics() {
        logger.info("===== Booking Metrics Report =====");
        logger.info("Success Rate: {:.2f}%", metrics.getSuccessRate() * 100);
        logger.info("Average Processing Time: {:.2f}ms", metrics.getAverageProcessingTime());
        logger.info("Concurrent Booking Attempts: {}", metrics.getConcurrentBookingCount());
        logger.info("================================");
    }

    /**
     * Attempts to book a specific room for a guest.
     * Checks room availability before confirming the booking.
     */
    public static void bookRoom(int roomNumber, String guest) {
        Room room = roomService.getRoom(roomNumber);
        if (room != null && room.getAvailableRooms() > 0) {
            roomService.bookRoom(roomNumber);
            logger.info("Room {} booked by {}", roomNumber, guest);
        } else {
            logger.warn("Room {} is not available for {}", roomNumber, guest);
        }
    }

    /**
     * Reads booking requests from a JSON file and adds them to the processing queue.
     * Implements a delay between requests to simulate real-world booking patterns.
     */
    public static void readBookingRequests(String filename) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<BookingRequest> requests = mapper.readValue(new File(filename), new TypeReference<List<BookingRequest>>() {
            });

            for (BookingRequest request : requests) {
                try {
                    incomingRequests.add(request);
                    Thread.sleep(2000); // Simulate real-world booking delay
                } catch (InterruptedException e) {
                    logger.error("Interrupted while reading booking requests", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (IOException e) {
            logger.error("Error reading booking requests: {}", e.getMessage());
        }
    }
}
