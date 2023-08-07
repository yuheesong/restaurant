package com.restaurant.controller;


import com.restaurant.dto.DateDto;
import com.restaurant.dto.ReservationFormDto;
import com.restaurant.dto.RestFormDto;
import com.restaurant.dto.findReDto;
import com.restaurant.entity.Rest;
import com.restaurant.service.ReservationService;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final RestService restService;

    @GetMapping(value ="restaurant/{rsId}/reservation")
    public String Home(Model model,@PathVariable("rsId") Long rsId){
        RestFormDto restDtl = restService.getRestDtl(rsId);
        model.addAttribute("rest", restDtl);
        return "reservation/reservation";
    }


    //DB에서 시간확인
    @PostMapping("/reservationtime")
    @ResponseBody
    public int makeReservation(@RequestBody DateDto datedto) {
        return reservationService.findTime(datedto);
    }


    //예약하기
    @PostMapping("/reservation/save")
    public String  createReservation(@RequestBody ReservationFormDto formDto) {

       reservationService.createReservation(formDto);

       return "index";

    }

    //예약내역
    @GetMapping("/mypage/{memberId}/reservation-details.html")
    public String findReservations(Model model,@PathVariable("memberId") int memberId,Pageable pageable ){

        Page<findReDto> reservations = reservationService.findReservations(memberId,pageable);
        String currentTime = reservationService.getCurrentTime();
        System.out.println(reservations);

        model.addAttribute("currentTime",currentTime);
        model.addAttribute("reservations",reservations);
        model.addAttribute("page",reservations);

        return "reservation-details";
    }

    @GetMapping("/reservation/cancel")
    @ResponseBody
    public int statusReservation(@RequestParam("re_id") int re_id){
        //예약상태=1; 예약취소상태=0;
        int status = reservationService.statusReservation(re_id);
        return status;
    }
}
