package com.booking.service;

import com.booking.exception.NotFoundException;
import com.booking.model.Booking;
import com.booking.model.DTO.BookingDTO;
import com.booking.model.DTO.BookingResponse;
import com.booking.model.mapper.BookingMapper;
import com.booking.persistence.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingRepository bookingRepository;


//    @Autowired
//    ClientPreferencesService clientPreferencesService;

//    @Autowired
//    ClientRepository clientRepository;

//    ClientPreferencesMapper clientPreferencesMapper = ClientPreferencesMapper.INSTANCE;

    BookingMapper bookingMapper = BookingMapper.INSTANCE;




    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream().map(booking -> bookingMapper.bookingToBookingResponse(booking)).collect(Collectors.toList());
    }

    @Override
    public BookingResponse findById(Long id) {
        return bookingMapper.bookingToBookingResponse(bookingRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public BookingResponse createBooking(BookingDTO bookingDTO) {

//        Client client = clientRepository.findById(bookingDTO.getClientId()).orElseThrow(NotFoundException::new);
//
////         Eliminar reservas antiguas si es necesario
//        client.getBookings().clear();


        Booking booking = bookingMapper.bookingDTOToBooking(bookingDTO);
//        booking.setClient(client);
        bookingRepository.save(booking);

//        client.getBookings().add(booking);
//
//        ClientPreferences clientPreferences = clientPreferencesMapper.clientPreferencesResponseToClientPreferences(clientPreferencesService.findById(booking.getClient().getId()));

//        updatePreferences(clientPreferences, booking);

        return bookingMapper.bookingToBookingResponse(booking);
    }

//    private void updatePreferences(ClientPreferences clientPreferences, Booking booking){
//        clientPreferences.setClientId(booking.getClient().getId());
//        clientPreferences.setTypeRoom(booking.getTypeRoom());
//        clientPreferences.setNumberPeople(booking.getNumberPeople());
//        clientPreferences.setPrice(booking.getPrice());
//    }

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
