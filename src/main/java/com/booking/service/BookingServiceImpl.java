package com.booking.service;

import com.booking.client.ClientPreferencesFeignClient;
import com.booking.exception.NotFoundException;
import com.booking.persistence.BookingRepository;
import com.commons.client.model.Booking;
import com.commons.client.model.ClientPreferences;
import com.commons.client.model.DTO.BookingDTO;
import com.commons.client.model.DTO.BookingResponse;
import com.commons.client.model.mappers.BookingMapper;
import com.commons.client.model.mappers.ClientPreferencesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.client.ClientFeignClient;
import com.commons.client.model.Client;
import com.commons.client.model.mappers.ClientMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ClientFeignClient clientFeignClient;

    @Autowired
    ClientPreferencesFeignClient clientPreferencesFeignClient;

    ClientPreferencesMapper clientPreferencesMapper = ClientPreferencesMapper.INSTANCE;

    BookingMapper bookingMapper = BookingMapper.INSTANCE;

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

        Client client = clientMapper.clientResponseToClient(clientFeignClient.findClientById(bookingDTO.getClientId()));

//         Eliminar reservas antiguas si es necesario
        client.getBookings().clear();


        Booking booking = bookingMapper.bookingDTOToBooking(bookingDTO);
        booking.setClient(client);
        bookingRepository.save(booking);

        client.getBookings().add(booking);

        ClientPreferences clientPreferences = clientPreferencesMapper.clientPreferencesResponseToClientPreferences(clientPreferencesFeignClient.findById(booking.getClient().getId()));

        updatePreferences(clientPreferences, booking);

        return bookingMapper.bookingToBookingResponse(booking);
    }

    private void updatePreferences(ClientPreferences clientPreferences, Booking booking){
        clientPreferences.setClientId(booking.getClient().getId());
        clientPreferences.setTypeRoom(booking.getTypeRoom());
        clientPreferences.setNumberPeople(booking.getNumberPeople());
        clientPreferences.setPrice(booking.getPrice());
    }

    @Override
    public void updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(NotFoundException::new);

        booking.setTypeRoom(bookingDTO.getTypeRoom() != null ? bookingDTO.getTypeRoom() : booking.getTypeRoom());
        booking.setNumberPeople(bookingDTO.getNumberPeople() != null ? bookingDTO.getNumberPeople() : booking.getNumberPeople());
        booking.setPrice(bookingDTO.getPrice() != null ? bookingDTO.getPrice() : booking.getPrice());
        booking.setEntryDate(bookingDTO.getEntryDate() != null ? bookingDTO.getEntryDate() : booking.getEntryDate());
        booking.setDepartureDate(bookingDTO.getDepartureDate() != null ? bookingDTO.getDepartureDate() : booking.getDepartureDate());

        bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {

        bookingRepository.delete(bookingRepository.findById(id).orElseThrow(NotFoundException::new));

    }
}
