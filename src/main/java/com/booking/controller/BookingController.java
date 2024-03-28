package com.booking.controller;

import com.booking.model.DTO.BookingDTO;
import com.booking.model.DTO.BookingResponse;
import com.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookingResponse>> findAll(){
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingResponse>findById(@PathVariable("id")Long id){
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingResponse>createBooking(@RequestBody BookingDTO bookingDTO){
        BookingResponse bookingResponse = bookingService.createBooking(bookingDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand((bookingResponse.getId()))
                .toUri();

        return ResponseEntity.created(location).body(bookingResponse);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>updateBooking(@PathVariable("id")Long id, @RequestBody BookingDTO bookingDTO){
        bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>deleteBooking(@PathVariable("id") Long id){
        bookingService.deleteBooking(id);
        return ResponseEntity.status(204).build();
    }


}