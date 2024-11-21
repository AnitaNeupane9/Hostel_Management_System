package com.codilien.hostelmanagement.exception;

public class RoomFullException extends RuntimeException{
    public RoomFullException(Long roomId) {
        super("Room with ID " + roomId + " is full. No available slots left.");
    }
}
