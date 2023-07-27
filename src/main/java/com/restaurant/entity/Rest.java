package com.restaurant.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="rest")
@Getter
@Setter
@ToString
public class Rest {
    @Id
    @Column(name="rest_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String restNm;

    @Column(name = "restPhone", nullable = false)
    private String restPhone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    @Lob
    @Column(nullable = false)
    private String introduction;

    @Lob
    @Column(nullable = false)
    private String restDetail;
}
