package com.restaurant.repository;

import com.restaurant.dto.MainRestDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.Rest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestRepositoryCustom {
    Page<Rest> getAdminRestPage(RestSearchDto restSearchDto, Pageable pageable);
    Page<MainRestDto> getMainRestPage(RestSearchDto restSearchDto, Pageable pageable);
    Page<MainRestDto> getCategoryRestPage(String category,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithSeoul(Pageable pageable);
    Page<MainRestDto> getRegionRestPage(String region,Pageable pageable);
}
