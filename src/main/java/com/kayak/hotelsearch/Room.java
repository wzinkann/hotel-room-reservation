package com.kayak.hotelsearch;

public class Room {
    private int roomNumber;
    private boolean isAvailable;
    private String currentGuest;
    private String checkInDate;
    private String checkOutDate;
    private int availableRooms;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isAvailable = true;
        this.currentGuest = null;
        this.checkInDate = null;
        this.checkOutDate = null;
        this.availableRooms = 1;
    }

    public Room(int roomNumber, String currentGuest, int availableRooms) {
        this.roomNumber = roomNumber;
        this.isAvailable = true;
        this.currentGuest = currentGuest;
        this.checkInDate = null;
        this.checkOutDate = null;
        this.availableRooms = availableRooms;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCurrentGuest() {
        return currentGuest;
    }

    public void setCurrentGuest(String currentGuest) {
        this.currentGuest = currentGuest;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public boolean bookRoom() {
        if (isAvailable && availableRooms > 0) {
            isAvailable = false;
            availableRooms--;
            return true;
        }
        return false;
    }

    public void releaseRoom() {
        isAvailable = true;
        availableRooms++;
        currentGuest = null;
        checkInDate = null;
        checkOutDate = null;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", isAvailable=" + isAvailable +
                ", currentGuest='" + currentGuest + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", availableRooms=" + availableRooms +
                '}';
    }
} 