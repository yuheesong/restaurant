package com.restaurant.controller;

import com.restaurant.dto.MainRestDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RestService restService;
    @GetMapping(value = "/")
    public String main(RestSearchDto restSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,16);
        Page<MainRestDto> rests =
                restService.getMainRestPage(restSearchDto, pageable);
        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        return "main";
    }
}
