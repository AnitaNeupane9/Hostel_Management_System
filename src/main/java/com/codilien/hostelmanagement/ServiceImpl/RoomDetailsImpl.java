package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.DTO.RoomDetailsDto;
import com.codilien.hostelmanagement.mapper.RoomDetailsMapper;
import com.codilien.hostelmanagement.model.RoomDetails;
import com.codilien.hostelmanagement.repository.RoomDetailsRepository;
import com.codilien.hostelmanagement.service.RoomDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomDetailsImpl implements RoomDetailsService {

    private RoomDetailsRepository roomDetailsRepository;

    public RoomDetailsImpl(RoomDetailsRepository roomDetailsRepository) {
        this.roomDetailsRepository = roomDetailsRepository;
    }


    @Override
    public RoomDetailsDto createRoom(RoomDetailsDto roomDetailsDto) {
        RoomDetails roomDetails = RoomDetailsMapper.mapToRoomDetails(roomDetailsDto);

        // Setting currentStudentCount to 0
        roomDetails.setCurrentStudentCount(0);
        // Calculate available slots as capacity - currentStudentCount
        roomDetails.setAvailableStudentSlots(roomDetails.getCapacity());

        // throw exception if the roomnumber is already present in db
        if (roomDetailsRepository.existsByRoomNumber(roomDetails.getRoomNumber())){
            return null;
        }

        RoomDetails savedRoom = roomDetailsRepository.save(roomDetails);
        return RoomDetailsMapper.mapToRoomDetailsDto(savedRoom);
    }

    @Override
    public RoomDetailsDto getRoom(Long id) {
       RoomDetails roomDetails = roomDetailsRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Room doesn't exist."));
       return RoomDetailsMapper.mapToRoomDetailsDto(roomDetails);
    }

    @Override
    public RoomDetailsDto updateRoom(Long id, RoomDetailsDto roomDetailsDto) {
        RoomDetails existingRoom = roomDetailsRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Room doesn't exist."));
        existingRoom.setRoomNumber(roomDetailsDto.getRoomNumber());
        existingRoom.setFloor(roomDetailsDto.getFloor());
        existingRoom.setRoomType(roomDetailsDto.getRoomType());
        existingRoom.setCapacity(roomDetailsDto.getCapacity());
        existingRoom.setCurrentStudentCount(roomDetailsDto.getCurrentStudentCount());
        existingRoom.setAvailableStudentSlots(roomDetailsDto.getAvailableStudentSlots());
        existingRoom.setStatus(roomDetailsDto.getStatus());

        RoomDetails updatedRoom = roomDetailsRepository.save(existingRoom);
        return RoomDetailsMapper.mapToRoomDetailsDto(updatedRoom);
    }

    @Override
    public List<RoomDetailsDto> getAllRooms() {
        List<RoomDetails> roomDetails = roomDetailsRepository.findAll();
        return roomDetails.stream()
                .map(rooms-> RoomDetailsMapper.mapToRoomDetailsDto(rooms))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoom(Long id) {
        RoomDetails roomDetails = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room doesn't exist."));
        roomDetailsRepository.delete(roomDetails);
    }
}
