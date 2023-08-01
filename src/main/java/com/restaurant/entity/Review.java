package com.restaurant.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rest rest;

    private String comment;

    private int star;
}
