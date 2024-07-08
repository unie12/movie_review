package com.example.movie_review.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String detailAddress;
    private String zipcode;

    protected Address() {}

    public Address(String city, String detailAddress, String zipcode) {
        this.city = city;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
    }
}
