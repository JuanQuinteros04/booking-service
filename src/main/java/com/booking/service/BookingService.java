package com.booking.service;

import com.booking.model.DTO.BookingDTO;
import com.booking.model.DTO.BookingResponse;

import java.util.List;

public interface BookingService {

    List<BookingResponse> findAll();

    BookingResponse findById(Long id);

    BookingResponse createBooking(BookingDTO bookingDTO);

    void updateBooking(Long id,BookingDTO bookingDTO);

    void deleteBooking(Long id);


}