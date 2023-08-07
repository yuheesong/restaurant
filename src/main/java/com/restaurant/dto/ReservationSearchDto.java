package com.restaurant.dto;

import lombok.Data;

@Data
public class ReservationSearchDto { //식당 데이터 조회시 식당 조회 조건을 갖고있는 dto 클래스
    private String searchDataType;

    private String searchBy;

    private String searchQuery = "";
}
