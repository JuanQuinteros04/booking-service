package com.booking.client;

import com.commons.room.model.DTO.RoomDTO;
import com.commons.room.model.DTO.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "room-service", url = "localhost:2002")
public interface RoomFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    List<RoomResponse> findAllRooms();

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{id}")
    RoomResponse findRoomById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/room/{roomNumber}")
    RoomResponse findByRoomNumber(@PathVariable("roomNumber") Long roomNumber);

    @RequestMapping(method = RequestMethod.POST, value = "/rooms")
    RoomResponse createRoom(RoomDTO roomDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/rooms/{id}")
    void updateRoom(@PathVariable("id") Long id, RoomDTO roomDTO);

    @RequestMapping(method = RequestMethod.DELETE, value = "/rooms/{id}")
    void deleteRoom(@PathVariable("id") Long id);
}
