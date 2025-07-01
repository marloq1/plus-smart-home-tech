package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {


    @Id
    @Column(name = "address_id", nullable = false, length = 255)
    private String addressId;

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
