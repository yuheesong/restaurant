package com.restaurant.repository;


import com.restaurant.dto.ReservationSearchDto;
import com.restaurant.dto.findReDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservarionRepositoryCustom {
    Page<findReDto> searchPageSimple(ReservationSearchDto condition, Pageable pageable);

    Page<findReDto> searchPageComplex(ReservationSearchDto condition, Pageable pageable);


}
