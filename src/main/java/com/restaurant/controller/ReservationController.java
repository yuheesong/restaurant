package com.restaurant.controller;


import com.restaurant.dto.DateDto;
import com.restaurant.dto.ReservationFormDto;
import com.restaurant.dto.RestFormDto;
import com.restaurant.entity.Member;
import com.restaurant.entity.ReView;
import com.restaurant.entity.Reservation;
import com.restaurant.service.ReservationService;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value ="rest/{rsId}/reservation")
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
        System.out.println(formDto+"111");

       reservationService.createReservation(formDto);

        return "redirect:/mypage/reservation";

    }

    //예약조회
    @GetMapping("/mypage/reservation")
    public String findReservations(Model model,Pageable pageable ){
        Member member = reservationService.findMember();
        Page<Reservation> reservations = reservationService.findReservations(member,pageable);
        model.addAttribute("reservations",reservations.getContent());
        model.addAttribute("page",reservations);

        return "reservation/reservation-details";
    }

    @GetMapping("/reservation/cancel")
    @ResponseBody
    public int statusReservation(@RequestParam("re_id") int re_id){
        //예약상태=1; 예약취소상태=0;
        int status = reservationService.statusReservation(re_id);
        return status;
    }

    @PostMapping("/riview")
    @ResponseBody
    public ResponseEntity<String> submitReview(@RequestParam("star") int star,
                                               @RequestParam("re_id") Long re_id) {
        ReView reView = reservationService.submitReview(re_id,star);
        System.out.println(reView +"리뷰저장성공");
        return ResponseEntity.ok("리뷰가 성공적으로 저장되었습니다.");
    }
}
