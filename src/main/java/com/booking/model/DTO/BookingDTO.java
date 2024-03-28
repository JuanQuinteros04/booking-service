package com.booking.model.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
public class BookingDTO {

    private String typeRoom;

    private Long numberPeople;

    private LocalDate entryDate;

    private LocalDate departureDate;

    private Double price;

    private Long clientId;
}
