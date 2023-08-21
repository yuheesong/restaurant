package com.restaurant.service;

import com.restaurant.entity.Member;
import com.restaurant.entity.Star;
import com.restaurant.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StarService {

    @Autowired
    StarRepository starRepository;

    public void Star(Star star){

        starRepository.save(star);
    }
}
