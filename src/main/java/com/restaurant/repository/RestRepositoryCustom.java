package com.restaurant.repository;

import com.restaurant.dto.MainRestDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.Rest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestRepositoryCustom {
    Page<Rest> getAdminRestPage(RestSearchDto restSearchDto, Pageable pageable);
    Page<MainRestDto> getMainRestPage(RestSearchDto restSearchDto, Pageable pageable);
    Page<MainRestDto> getCategoryRestPage(String category,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithSeoul(Pageable pageable);
    Page<MainRestDto> getRegionRestPage(String region,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithGyeongin(List<String> regions,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithGangwon(List<String> regions,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithChungcheong(List<String> regions,Pageable pageable);
    Page<MainRestDto> findByAddressStartingWithDaejeon(List<String> regions,Pageable pageable);
}
