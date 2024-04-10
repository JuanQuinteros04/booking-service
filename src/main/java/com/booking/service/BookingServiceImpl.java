package com.booking.service;

import com.booking.client.ClientPreferencesFeignClient;
import com.booking.client.RoomFeignClient;
import com.booking.exception.NotFoundException;
import com.booking.persistence.BookingRepository;
import com.commons.booking.model.Booking;
import com.commons.preferences.model.ClientPreferences;
import com.commons.booking.model.DTO.BookingDTO;
import com.commons.booking.model.DTO.BookingResponse;
import com.commons.client.model.DTO.ClientResponse;
import com.commons.booking.model.mapper.BookingMapper;
import com.commons.preferences.model.DTO.ClientPreferencesDTO;
import com.commons.preferences.model.mapper.ClientPreferencesMapper;
import com.commons.room.model.DTO.RoomResponse;
import com.commons.room.model.Room;
import com.commons.room.model.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.client.ClientFeignClient;
import com.commons.client.model.Client;
import com.commons.client.model.mappers.ClientMapper;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ClientFeignClient clientFeignClient;

    @Autowired
    RoomFeignClient roomFeignClient;

    @Autowired
    ClientPreferencesFeignClient clientPreferencesFeignClient;

    ClientPreferencesMapper clientPreferencesMapper = ClientPreferencesMapper.INSTANCE;

    BookingMapper bookingMapper = BookingMapper.INSTANCE;

    RoomMapper roomMapper = RoomMapper.INSTANCE;

    ClientMapper clientMapper = ClientMapper.INSTANCE;


    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream().map(booking -> bookingMapper.bookingToBookingResponse(booking)).collect(Collectors.toList());
    }

    @Override
    public BookingResponse findById(Long id) {
//        return bookingMapper.bookingToBookingResponse(bookingRepository.findById(id).orElseThrow(NotFoundException::new));
        return bookingMapper.bookingToBookingResponse(bookingRepository.findById(id).orElseThrow(NotFoundException::new));
    }
    @Override
    public BookingResponse createBooking(BookingDTO bookingDTO) {

        // Creamos un ClientResponse utilizando Feign Client recuperamos el cliente con el clientId que nos pasa el usuario por bookingDTO
        ClientResponse clientResponse = clientFeignClient.findClientById(bookingDTO.getClientId());
        RoomResponse roomResponse = roomFeignClient.findByRoomNumber(bookingDTO.getRoomNumber());


        // Comprobamos que exista un client con el id del atributo booking(clientId)
        if (clientResponse != null) {

            // Crea un Client utilizando el mapeador
            Client client = clientMapper.clientResponseToClient(clientResponse);

            // Crear la reserva
            Booking booking = bookingMapper.bookingDTOToBooking(bookingDTO);
            if (roomResponse != null){
                long totalDays = ChronoUnit.DAYS.between(bookingDTO.getEntryDate(), bookingDTO.getDepartureDate());
                Room room = roomMapper.roomResponseToRoom(roomResponse);
                booking.setTotalPrice(room.getPricePerNight() * totalDays);
            }else {
                throw new IllegalArgumentException("Room not found.");
            }

            // La guarda
            bookingRepository.save(booking);

            // Creamos una preferencia del cliente en base al id del client
            ClientPreferences clientPreferences = clientPreferencesMapper.clientPreferencesResponseToClientPreferences(clientPreferencesFeignClient.findByClientId(client.getId()));

            // Comprobamos si existe un objeto clientPreferences con el id del client
            if (clientPreferences != null){
                // En caso de que exista, mediante Feign client accedemos al metodo actualizar(updateClientPreferences)
                clientPreferencesFeignClient.deleteClientPreferences(clientPreferences.getId());
            }else {
                // Si no existe una preferencia del cliente con el id de Client creamos una.
                clientPreferencesFeignClient.createClientPreferences(createPreferences(booking));
            }

            return bookingMapper.bookingToBookingResponse(booking);

        } else {
            throw new IllegalArgumentException("Client not found.");
        }
    }

    private ClientPreferencesDTO createPreferences(Booking booking){

        ClientPreferencesDTO clientPreferencesDTO = new ClientPreferencesDTO();

        long totalDays = ChronoUnit.DAYS.between(booking.getEntryDate(), booking.getDepartureDate());


        clientPreferencesDTO.setClientId(booking.getClientId());
        clientPreferencesDTO.setTypeRoom(booking.getTypeRoom());
        clientPreferencesDTO.setNumberPeople(booking.getRoomCapacity());
        clientPreferencesDTO.setPrice(booking.getTotalPrice() / totalDays);

        return clientPreferencesDTO;
    }

//    @Override
//    public void updateBooking(Long id, BookingDTO bookingDTO) {
//        Booking booking = bookingRepository.findById(id).orElseThrow(NotFoundException::new);
//
//        booking.setRoomNumber(bookingDTO.getRoomNumber() != null ? bookingDTO.getRoomNumber() : booking.getRoomNumber());
//
////        booking.setTypeRoom(bookingDTO.getTypeRoom() != null ? bookingDTO.getTypeRoom() : booking.getTypeRoom());
////        booking.setRoomCapacity(bookingDTO.getNumberPeople() != null ? bookingDTO.getNumberPeople() : booking.getNumberPeople());
////        booking.setPrice(bookingDTO.getPrice() != null ? bookingDTO.getPrice() : booking.getPrice());
////        booking.setEntryDate(bookingDTO.getEntryDate() != null ? bookingDTO.getEntryDate() : booking.getEntryDate());
////        booking.setDepartureDate(bookingDTO.getDepartureDate() != null ? bookingDTO.getDepartureDate() : booking.getDepartureDate());
//
//        bookingRepository.save(booking);
//    }

    @Override
    public void deleteBooking(Long id) {

        bookingRepository.delete(bookingRepository.findById(id).orElseThrow(NotFoundException::new));

    }
}
