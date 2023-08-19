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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RestService restService;
    @GetMapping(value = "/")
    public String main(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests = restService.getMainRestPage(restSearchDto, pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "main";
    }
    @GetMapping(value = "/rest/category")
    public String CategoryMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getCategory() != null && !restSearchDto.getCategory().isEmpty()) {
            rests = restService.getCategoryRestPage(restSearchDto.getCategory(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "categoryMain";
    }

}
