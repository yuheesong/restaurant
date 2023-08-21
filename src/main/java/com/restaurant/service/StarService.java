package com.restaurant.service;

import com.restaurant.entity.Member;
import com.restaurant.entity.Star;
import com.restaurant.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;

    public void Star(Star star){

        starRepository.save(star);
    }
}
