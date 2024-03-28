package com.booking.model.mapper;

import com.booking.model.Booking;
import com.booking.model.DTO.BookingDTO;
import com.booking.model.DTO.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "id", ignore = true)
    Booking bookingDTOToBooking(BookingDTO bookingDTO);

    BookingResponse bookingToBookingResponse (Booking booking);


}
