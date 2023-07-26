package com.restaurant.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class StarItem {
    @Id
    @GeneratedValue
    @Column(name = "star_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star_id")
    private Star star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Rest rest;
}
