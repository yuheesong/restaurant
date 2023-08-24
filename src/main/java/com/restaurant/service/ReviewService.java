package com.restaurant.service;

import com.restaurant.entity.Member;
import com.restaurant.entity.Rest;
import com.restaurant.repository.ReViewRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReViewRepositoryImpl repository;

    public String findRiView(Long id){
        String formattedRiView ="";
        Double riView = repository.findRiView(id);
        if (riView!=null) {
            formattedRiView = String.format("%.2f", riView);
        }else {
            formattedRiView="0";
        }
        return formattedRiView;
    }
    public long save(Long re_id,Long i){
        long save = repository.save(re_id, i);
       return save;
    }

}
