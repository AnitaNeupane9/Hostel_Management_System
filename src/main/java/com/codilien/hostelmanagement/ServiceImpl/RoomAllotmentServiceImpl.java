package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.exception.RoomFullException;
import com.codilien.hostelmanagement.exception.RoomNotFoundException;
import com.codilien.hostelmanagement.exception.StudentNotFoundException;
import com.codilien.hostelmanagement.DTO.RoomAllotmentDto;
import com.codilien.hostelmanagement.Enum.RoomStatus;
import com.codilien.hostelmanagement.mapper.RoomAllotmentMapper;
import com.codilien.hostelmanagement.model.RoomAllotment;
import com.codilien.hostelmanagement.model.RoomDetails;
import com.codilien.hostelmanagement.model.Student;
import com.codilien.hostelmanagement.repository.RoomAllotmentRepository;
import com.codilien.hostelmanagement.repository.RoomDetailsRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import com.codilien.hostelmanagement.service.RoomAllotmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomAllotmentServiceImpl implements RoomAllotmentService {


    private StudentRepository studentRepository;
    private RoomDetailsRepository roomDetailsRepository;
    private RoomAllotmentRepository roomAllotmentRepository;

    @Autowired
    public RoomAllotmentServiceImpl(StudentRepository studentRepository, RoomDetailsRepository roomDetailsRepository, RoomAllotmentRepository roomAllotmentRepository) {
        this.studentRepository = studentRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomAllotmentRepository = roomAllotmentRepository;
    }

    @Override
    @Transactional
    public RoomAllotmentDto allotRoom(RoomAllotmentDto roomAllotmentDto) {

        RoomAllotment roomAllotment = RoomAllotmentMapper.mapToRoomAllotment(roomAllotmentDto);

        // Fetch the student, throw exception if not found
        Student student = studentRepository.findById(roomAllotmentDto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(roomAllotmentDto.getStudentId()));

//        System.out.println("Student found: " + student);

        // Fetch the room details, throw specific exception if not found
        RoomDetails roomDetails = roomDetailsRepository.findById(roomAllotmentDto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(roomAllotmentDto.getRoomId()));

//        System.out.println("Room Found: " + roomDetails);

        // Check if the room has available slots, throw exception if room is full
        if (roomDetails.getAvailableStudentSlots() <= 0) {
            throw new RoomFullException(roomAllotmentDto.getRoomId());
        }

        // Check if the room status is ACTIVE
        if (roomDetails.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room cannot be allotted. Status is " + roomDetails.getStatus());
        }
        // Set the actual Student and RoomDetails objects in the RoomAllotment entity
        roomAllotment.setStudent(student);
        roomAllotment.setRoomDetails(roomDetails);

        roomDetails.setCurrentStudentCount(roomDetails.getCurrentStudentCount() + 1);
        roomDetails.setAvailableStudentSlots(roomDetails.getAvailableStudentSlots() - 1);

        // Save RoomDetails first
        roomDetailsRepository.save(roomDetails);

        RoomAllotment savedRoomAllotment = roomAllotmentRepository.save(roomAllotment);
        return RoomAllotmentMapper.mapToRoomAllotmentDto(savedRoomAllotment);
    }

    @Override
    public RoomAllotmentDto getRoomAllotment(Long id) {
        RoomAllotment roomAllotment = roomAllotmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Allotment doesn't exist."));
        return RoomAllotmentMapper.mapToRoomAllotmentDto(roomAllotment);
    }

    @Override
    public List<RoomAllotmentDto> getAllRoomAllotment() {
        List<RoomAllotment> roomAllotments = roomAllotmentRepository.findAll();
        return roomAllotments.stream()
                .map(allotments -> RoomAllotmentMapper.mapToRoomAllotmentDto(allotments))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomAllotmentDto updateRoomAllotment(Long id, RoomAllotmentDto roomAllotmentDto) {
        RoomAllotment toBeUpdatedAllotment = roomAllotmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Allotment doesn't exist."));

        // Store the old RoomDetails
        RoomDetails oldRoom = toBeUpdatedAllotment.getRoomDetails();

        toBeUpdatedAllotment.setAllotmentDate(roomAllotmentDto.getAllotmentDate());
        toBeUpdatedAllotment.setDuration(roomAllotmentDto.getDuration());
        toBeUpdatedAllotment.setCheckIn(roomAllotmentDto.getCheckIn());
        toBeUpdatedAllotment.setCheckOut(roomAllotmentDto.getCheckOut());


        if (roomAllotmentDto.getStudentId() != null && !roomAllotmentDto.getStudentId().equals(toBeUpdatedAllotment.getStudent().getId())) {
            Student student = new Student();
            student.setId(roomAllotmentDto.getStudentId());
            toBeUpdatedAllotment.setStudent(student);
        }

        if (roomAllotmentDto.getRoomId() != null && !roomAllotmentDto.getRoomId().equals(toBeUpdatedAllotment.getRoomDetails().getId())) {
            // Getting the new RoomDetails object
            RoomDetails newRoom = new RoomDetails();
            newRoom.setId(roomAllotmentDto.getRoomId());
            toBeUpdatedAllotment.setRoomDetails(newRoom);

            // Updating the old room's currentStudentCount and availableStudentSlots
            if (oldRoom != null) {
                oldRoom.setCurrentStudentCount(oldRoom.getCurrentStudentCount() - 1);
                oldRoom.setAvailableStudentSlots(oldRoom.getAvailableStudentSlots() + 1);
                roomDetailsRepository.save(oldRoom);
            }

            // Updating the new room's currentStudentCount and availableStudentSlots
            RoomDetails updatedNewRoom = roomDetailsRepository.findById(newRoom.getId())
                    .orElseThrow(() -> new RuntimeException("Room not found."));
            updatedNewRoom.setCurrentStudentCount(updatedNewRoom.getCurrentStudentCount() + 1);
            updatedNewRoom.setAvailableStudentSlots(updatedNewRoom.getAvailableStudentSlots() - 1);
            roomDetailsRepository.save(updatedNewRoom);
        }

        RoomAllotment updatedAllotment = roomAllotmentRepository.save(toBeUpdatedAllotment);
        return RoomAllotmentMapper.mapToRoomAllotmentDto(updatedAllotment);
    }


    @Override
    @Transactional
    public void deleteRoomAllotment(Long id) {

        RoomAllotment toBeDeletedAllotment = roomAllotmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room allotment doesn't exist."));

        RoomDetails roomDetails = toBeDeletedAllotment.getRoomDetails();

        if (roomDetails != null) {
            // decrease currentStudentCount and increase availableStudentSlots
            roomDetails.setCurrentStudentCount(roomDetails.getCurrentStudentCount() - 1);
            roomDetails.setAvailableStudentSlots(roomDetails.getAvailableStudentSlots() + 1);
            roomDetailsRepository.save(roomDetails);
        }
        roomAllotmentRepository.delete(toBeDeletedAllotment);
    }
}
