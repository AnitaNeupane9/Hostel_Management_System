package com.codilien.hostelmanagement.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long roomId) {
        super("Room with ID " + roomId + " not found. Please ensure the room exists.");
    }
}
