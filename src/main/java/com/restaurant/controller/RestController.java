package com.restaurant.controller;

import com.restaurant.dto.RestFormDto;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RestController {
    private final RestService restService;
    @GetMapping(value = "/admin/rest/new")
    public String itemForm(Model model){
        model.addAttribute("restFormDto", new RestFormDto());
        return "rest/restForm";
    }

    @PostMapping(value = "/admin/rest/new")
    public String restNew(@Valid RestFormDto restFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("restImgFile") List<MultipartFile> restImgFileList){

        if(bindingResult.hasErrors()){
            return "rest/restForm";
        }

        if(restImgFileList.get(0).isEmpty() && restFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 식당 이미지는 필수 입력 값 입니다.");
            return "rest/restForm";
        }

        try {
            restService.saveRest(restFormDto, restImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "식당 등록 중 에러가 발생하였습니다.");
            return "rest/restForm";
        }

        return "redirect:/";
    }
}
