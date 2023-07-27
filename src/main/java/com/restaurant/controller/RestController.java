package com.restaurant.controller;

import com.restaurant.dto.RestFormDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.Rest;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/admin/rest/{restId}")
    public String restDtl(@PathVariable("restId") Long restId, Model model){

        try {
            RestFormDto restFormDto = restService.getRestDtl(restId);
            model.addAttribute("restFormDto", restFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 식당 입니다.");
            model.addAttribute("restFormDto", new RestFormDto());
            return "rest/restForm";
        }

        return "rest/restForm";
    }

    @PostMapping(value = "/admin/rest/{restId}")
    public String restUpdate(@Valid RestFormDto restFormDto, BindingResult bindingResult,
                             @RequestParam("restImgFile") List<MultipartFile> restImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "rest/restForm";
        }

        if(restImgFileList.get(0).isEmpty() && restFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 식당 이미지는 필수 입력 값 입니다.");
            return "rest/restForm";
        }

        try {
            restService.updateRest(restFormDto, restImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "식당 수정 중 에러가 발생하였습니다.");
            return "rest/restForm";
        }

        return "redirect:/";
    }
    @GetMapping(value = {"/admin/rests", "/admin/rests/{page}"})
    public String restManage(RestSearchDto restSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,10);
        Page<Rest> rests =
                restService.getAdminRestPage(restSearchDto, pageable);
        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        return "rest/restMng";
    }

}
