package com.restaurant.service;


import com.restaurant.dto.DateDto;
import com.restaurant.dto.ReservationFormDto;
import com.restaurant.entity.Member;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.Rest;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {


    private final ReservationRepository reservationRepository;


    private final RestRepository restRepository;

    private final ReservationRepositoryImpl repository;


    private final MemberRepository memberRepository;

    //넘어온 식당id를 가지고 해당날짜와 시간대에 예약이있나 확인하는 서비스
    public List<HashMap<String, Integer>> findByRsid(int id) {
        List<Reservation> re = reservationRepository.findByRsId(id);
        List<HashMap<String, Integer>> asd = new ArrayList<>();
        for (int i = 0; i < re.size(); i++) {
            HashMap<String, Integer> ss = new HashMap<>();
            ss.put("년", re.get(i).getCreate_date().getYear() + 1900);
            ss.put("월", re.get(i).getCreate_date().getMonth() + 1);
            ss.put("일", re.get(i).getCreate_date().getDate());
            ss.put("시", re.get(i).getCreate_date().getHours());
            asd.add(ss);
        }
        return asd;
    }


    public int findTime(@RequestBody DateDto datedto) {

        int status = 0;

        int id = datedto.getId();
        int year = datedto.getYear();
        int month = datedto.getMonth();
        int day = datedto.getDay();
        int hour = datedto.getHour();
        // LocalDateTime 객체 생성
        LocalDateTime targetDateTime = LocalDateTime.of(year, month, day, hour, 0, 0);
        LocalDateTime now = LocalDateTime.now();

        // DateTimeFormatter를 사용하여 형식화된 문자열 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatetime = targetDateTime.format(formatter);

        // formattedDatetime을 LocalDateTime으로 변환하여 쿼리 실행
        LocalDateTime parsedDatetime = LocalDateTime.parse(formattedDatetime, formatter);

        // LocalDateTime을 java.util.Date로 변환
        Date date = Date.from(parsedDatetime.atZone(ZoneId.systemDefault()).toInstant());

        String result = reservationRepository.findTime(date, id);

        if (result != null) {
            status = 1;
        } else {
            if (now.isAfter(parsedDatetime)) {
                status = 1;
            }
        }

            return status;
        }
    //예약하기
    public void createReservation(@RequestBody ReservationFormDto formDto) {
        Rest foundRestaurant =null;
        Reservation reservation = new Reservation();

        Member member = findMember();
        Optional<Rest> restaurant = restRepository.findById(formDto.getId());
        if (restaurant.isPresent()) {
            foundRestaurant = restaurant.get();
        }


        int year = formDto.getYear();
        int month = formDto.getMonth();
        int day = formDto.getDay();
        int hour = formDto.getHour();
        // LocalDateTime 객체 생성
        LocalDateTime targetDateTime = LocalDateTime.of(year, month, day, hour, 0, 0);

        // DateTimeFormatter를 사용하여 형식화된 문자열 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatetime = targetDateTime.format(formatter);

        // formattedDatetime을 LocalDateTime으로 변환하여 쿼리 실행
        LocalDateTime parsedDatetime = LocalDateTime.parse(formattedDatetime, formatter);

        // LocalDateTime을 java.util.Date로 변환
        Date date = Date.from(parsedDatetime.atZone(ZoneId.systemDefault()).toInstant());
        //rv.setM_id(); 세션추가되면 삽입
        reservation.setRe_member(member);
        reservation.setCreate_date(date);
        reservation.setReservation_status(1);
        reservation.setRequest(formDto.getInputValue());
        reservation.setPeople(formDto.getCount());
        reservation.setReservation_status(1);
        reservation.setRe_restaurant(foundRestaurant);
        System.out.println(reservation+"qq");
        reservationRepository.save(reservation);
    }

    public Page<Reservation> findReservations(Member memberId, Pageable pageable){
        return repository.findReservations(memberId,pageable);
    }

    public int statusReservation(int re_id){
        int st = repository.statusReservation(re_id);

        return st;
    }
    public String getCurrentTime() {
        // 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();

        // 시간 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷에 맞게 현재 시간을 문자열로 변환
        String formattedTime = currentTime.format(formatter);

        // 프론트엔드로 현재 시간 문자열 반환
        return formattedTime;
    }

    public Member findMember(){
        Member member=null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                member = memberRepository.findByEmail(username);
            }
        }
        return member;
    }


}
