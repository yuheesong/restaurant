package com.restaurant.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="review")
@Getter
@Setter
@ToString
public class ReView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private int comment;

    private Date create_date;
    private Date delete_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Rest rest;
}
