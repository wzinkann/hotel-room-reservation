package com.kayak.hotelsearch.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.TimeUnit;

/**
 * Service for tracking booking-related metrics.
 * Uses Micrometer for metric collection and reporting.
 */
public class BookingMetrics {
    private final MeterRegistry registry;
    private final Counter successfulBookings;
    private final Counter failedBookings;
    private final Timer bookingProcessingTime;
    private final Counter concurrentBookings;

    public BookingMetrics() {
        this.registry = new SimpleMeterRegistry();
        
        // Initialize counters
        this.successfulBookings = Counter.builder("booking.successful")
            .description("Number of successful bookings")
            .register(registry);
            
        this.failedBookings = Counter.builder("booking.failed")
            .description("Number of failed bookings")
            .register(registry);
            
        this.concurrentBookings = Counter.builder("booking.concurrent")
            .description("Number of concurrent booking attempts")
            .register(registry);
            
        // Initialize timer
        this.bookingProcessingTime = Timer.builder("booking.processing.time")
            .description("Time taken to process booking requests")
            .register(registry);
    }

    /**
     * Records a successful booking attempt.
     */
    public void recordSuccessfulBooking() {
        successfulBookings.increment();
    }

    /**
     * Records a failed booking attempt.
     */
    public void recordFailedBooking() {
        failedBookings.increment();
    }

    /**
     * Records a concurrent booking attempt.
     */
    public void recordConcurrentBooking() {
        concurrentBookings.increment();
    }

    /**
     * Returns a timer for measuring booking processing time.
     */
    public Timer.Sample startBookingTimer() {
        return Timer.start(registry);
    }

    /**
     * Stops the booking timer and records the processing time.
     */
    public void stopBookingTimer(Timer.Sample sample) {
        sample.stop(bookingProcessingTime);
    }

    /**
     * Returns the current booking success rate.
     */
    public double getSuccessRate() {
        double total = successfulBookings.count() + failedBookings.count();
        return total > 0 ? successfulBookings.count() / total : 0.0;
    }

    /**
     * Returns the average booking processing time in milliseconds.
     */
    public double getAverageProcessingTime() {
        return bookingProcessingTime.mean(TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the total number of concurrent booking attempts.
     */
    public double getConcurrentBookingCount() {
        return concurrentBookings.count();
    }
} 