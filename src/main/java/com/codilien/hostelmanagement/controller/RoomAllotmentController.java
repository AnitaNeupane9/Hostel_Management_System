package com.codilien.hostelmanagement.controller;

import com.codilien.hostelmanagement.DTO.RoomAllotmentDto;
import com.codilien.hostelmanagement.service.RoomAllotmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomAllotments/")
public class RoomAllotmentController {

    @Autowired
    private RoomAllotmentService roomAllotmentService;

    public RoomAllotmentController(RoomAllotmentService roomAllotmentService) {
        this.roomAllotmentService = roomAllotmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoomAllotmentDto> createRoomAllotment(@RequestBody RoomAllotmentDto roomAllotmentDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(roomAllotmentService.allotRoom(roomAllotmentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomAllotmentDto> getRoomAllotment(@PathVariable Long id){
        return ResponseEntity.ok(roomAllotmentService.getRoomAllotment(id));
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<RoomAllotmentDto>> getAllAllotments(){
        List<RoomAllotmentDto> allotments = roomAllotmentService.getAllRoomAllotment();
        return ResponseEntity.ok(allotments);
    }

    @GetMapping("/{id}/update")
    public ResponseEntity<RoomAllotmentDto> updateRoomAllotments(@PathVariable Long id, @RequestBody RoomAllotmentDto roomAllotmentDto){
        return ResponseEntity.status(HttpStatus.OK).body(roomAllotmentService.updateRoomAllotment(id, roomAllotmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoomAllotment(@PathVariable Long id){
        roomAllotmentService.deleteRoomAllotment(id);
        return ResponseEntity.ok("Room Allotment is deleted Successfully.");
    }
}
