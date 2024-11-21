package com.codilien.hostelmanagement.controller;

import com.codilien.hostelmanagement.DTO.RoomDetailsDto;
import com.codilien.hostelmanagement.repository.RoomDetailsRepository;
import com.codilien.hostelmanagement.service.RoomDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomDetailsController {

    @Autowired
    private RoomDetailsService roomDetailsService ;
    @Autowired
    private RoomDetailsRepository roomDetailsRepository;

    public RoomDetailsController(RoomDetailsService roomDetailsService) {
        this.roomDetailsService = roomDetailsService;
    }


    @PostMapping("/create")
    public ResponseEntity<Object> addRoom (@RequestBody RoomDetailsDto roomDetailsDto, HttpServletRequest request) {
        RoomDetailsDto createdRoom = roomDetailsService.createRoom(roomDetailsDto);

        HttpSession session = request.getSession(false); // Get the session if it exists, don't create a new one

        if (session == null || session.getAttribute("role") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to create rooms.");
        }

        String role = (String) session.getAttribute("role");
//        Role role = Role.valueOf(roleName); // Convert String back to enum


        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Only ADMIN can create rooms.");
        }

        if (createdRoom == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room number already exists!");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailsDto> getRoomDetail (@PathVariable Long id){
        return ResponseEntity.ok(roomDetailsService.getRoom(id));
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<RoomDetailsDto>> getAllRooms(){
        List<RoomDetailsDto> roomDetails = roomDetailsService.getAllRooms();
        return ResponseEntity.ok(roomDetails);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<RoomDetailsDto> editRoomDetail(@PathVariable Long id, @RequestBody RoomDetailsDto roomDetailsDto){
        return ResponseEntity.status(HttpStatus.OK).body(roomDetailsService.updateRoom(id,roomDetailsDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id){
        roomDetailsService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted Successfully!");
    }
}
