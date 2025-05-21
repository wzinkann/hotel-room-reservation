# Room Booking System

## Interviewer Note
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

# Kayak Coding Challenge - Room Booking

In this exercise, you'll be presented the code that tries to book total of 10 rooms from different guests. The goal is to make the code work as intended. Also suggest areas of improvement and show us the improvements in the code.

## Notes:

* This is designed to be completed in 2 hours. Our suggestion is not to spend too much time on this challenge. 
* It is an open book exercise. You can use any tools and websites the way you would use in a real work environment.
* The code has to be compiled and run for each section, so that we can evaluate the output.
* If there is any part that is not clear to you, make a reasonable assumption, and state it in comment. Adjust code accordingly.
* The code has to run and finish gracefully no matter what.
* Make the output meaningful, and formatted in a way that is easy to read.

## Pre-requisites
- Java 21
- Maven 3.8 or higher
- IntelliJ IDEA or any other IDE of your choice

## Code Concept

- Main - the application's main class.
- Room - represents a hotel room type and its availability
- RoomDatabaseAccessService - provides db access to room data
- BookingRequest - contains a request to book a room

When the application starts, it will initialize rooms and their availability. It will then create a list of booking requests and try to book the rooms. The goal is to process multiple requests simultaneously, while ensuring that the rooms are booked correctly.

In this challenge, just do not make it "work", meaning it outputs something appears to be working. Instead, imagine that you are building a real website, where you have tens of thousands of users are trying to book rooms, while the system needs to maintain integrity. Demonstrate your ability to write code that would work in such a large, real-world system.

## Section 1 - Warming up

Make the app output your name to stderr and the answer to the work-from-office question to stdout. Read the code comment for details, and follow the instructions.

## Section 2 - Workable Code

Read the code, and based on the concept above, test and see if it works as intended. State what is the problem in std output, and fix it.

## Section 3 - Optimize the code

Given that code, and thinking in that this is a real website, what would you do to make it more performant?

## Section 4 - Find issues
There may be cases where the code works on the surface, but it is not correct and cause issues when handling with more data, more threads, more requests, etc. Find such potential issues and fix them.

## Section 5 - Bonus Points
### Bonus point 1
Find classes that can be immutable. Discuss if that's a good idea or not, and if so, implement it. If not, explain why.

### Bonus point 2
Write a thorough unit test to test for the RoomDatabaseAccessService class.

