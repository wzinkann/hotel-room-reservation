# Room Booking System

##
This project is a Java-based Room Booking System designed to manage hotel room bookings efficiently. The system allows users to create booking requests, check room availability, and track booking statuses. It is built with a focus on immutability and thread safety, making it suitable for concurrent operations.

### Key Features
- **Booking Requests:** The system allows users to create booking requests with guest information, room details, and booking status. The `BookingRequest` class is fully immutable, enhancing thread safety and predictability.
- **Room Management:** The application initializes rooms and their availability, allowing users to book or release rooms while maintaining data integrity.
- **Metrics Collection:** The system tracks successful and failed bookings, as well as processing times, providing valuable insights into booking performance.
- **Thread Safety:** The design ensures that concurrent bookings are handled correctly, preventing race conditions and ensuring data consistency.

### Design Choices
- **Immutability:** The decision to make the `BookingRequest` class immutable was made to enhance thread safety and eliminate potential race conditions. This approach ensures that the state of a booking request cannot be altered after creation, making the code more predictable and easier to reason about.
- **Concurrency Handling:** The system is designed to handle multiple booking requests simultaneously, ensuring that room availability is accurately maintained even under high load.

### Improvements Made
- **Code Optimization:** The code has been optimized for performance, ensuring that it can handle a large number of concurrent bookings efficiently.
- **Error Handling:** Robust error handling has been implemented to ensure that the application runs gracefully, even in the face of unexpected inputs or failures.
- **Testing:** Comprehensive unit tests have been written to ensure that the system behaves as expected under various conditions.

## Overview
The Room Booking System is a Java-based application designed to manage hotel room bookings. It allows users to create booking requests, check room availability, and track booking statuses. The system is built with a focus on immutability and thread safety, making it suitable for concurrent operations.

## Features
- **Booking Requests:** Create and manage booking requests with guest information, room details, and booking status.
- **Room Management:** Initialize rooms, check availability, and book or release rooms.
- **Metrics Collection:** Track successful and failed bookings, as well as processing times.
- **Thread Safety:** The system is designed to be thread-safe, ensuring that concurrent bookings are handled correctly.
- **Immutability:** The `BookingRequest` class is fully immutable, enhancing thread safety and predictability.

## Architecture Diagram
Below is an ASCII representation of the Room Booking System's architecture, illustrating the main components and their interactions:
    More detailed version of diagram in 'architecture_diagram.txt' file
```
+------------------+       +------------------+       +------------------+
|                  |       |                  |       |                  |
|  BookingRequest  |------>|  RoomDatabase    |------>|  Room            |
|                  |       |  AccessService   |       |                  |
+------------------+       +------------------+       +------------------+
        |                          |                          |
        v                          v                          v
+------------------+       +------------------+       +------------------+
|                  |       |                  |       |                  |
|  Metrics         |<------|  Main            |<------|  BookingSystem   |
|                  |       |                  |       |                  |
+------------------+       +------------------+       +------------------+
```

### Explanation
- **BookingRequest:** Represents a booking request with guest information and room details.
- **RoomDatabaseAccessService:** Provides access to room data, allowing for booking and releasing rooms.
- **Room:** Represents a hotel room and its availability.
- **Main:** The main application class that initializes the system and processes booking requests.
- **Metrics:** Tracks booking performance and provides insights into successful and failed bookings.
- **BookingSystem:** Manages the overall booking process, ensuring that rooms are booked correctly and efficiently.

This diagram helps visualize how the components interact within the system, highlighting the flow of data and the responsibilities of each component.
