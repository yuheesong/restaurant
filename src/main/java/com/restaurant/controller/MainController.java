package com.restaurant.controller;

import com.restaurant.dto.MainRestDto;
import com.restaurant.dto.RestFormDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
    @GetMapping(value = "/rest/region")
    public String RegionMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
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
        return "regionMain";
    }

    @GetMapping(value = "/rest/seoul")
    public String SeoulMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        // 서울로 시작하는 주소의 음식점만 가져오는 로직으로 변경
        Page<MainRestDto> rests = restService.getSeoulRestPage(pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto); // 이 부분은 검색 기능이 필요하면 그대로 두고, 필요 없다면 제거 가능
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/seoulPage";
    }

    @GetMapping(value = "/rest/seoul/region")
    public String SeoulRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/seoulPage";
    }

    @GetMapping(value = "/rest/gyeongin")
    public String GyeonginMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getGyeonginRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gyeonginPage";
    }
    @GetMapping(value = "/rest/gyeongin/region")
    public String GyeonginRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gyeonginPage";
    }

    @GetMapping(value = "/rest/gangwon")
    public String GangwonMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getGangwonRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gangwonPage";
    }
    @GetMapping(value = "/rest/gangwon/region")
    public String GangwonRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gangwonPage";
    }

    @GetMapping(value = "/rest/chungcheong")
    public String ChungcheongMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getChungcheongRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/chungcheongPage";
    }
    @GetMapping(value = "/rest/chungcheong/region")
    public String ChungcheongRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/chungcheongPage";
    }

    @GetMapping(value = "/rest/daejeon")
    public String DaejeonMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getDaejeonRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/daejeonPage";
    }
    @GetMapping(value = "/rest/daejeon/region")
    public String DaejeonRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/daejeonPage";
    }

    @GetMapping(value = "/rest/jeolla")
    public String JeollaMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getJeollaRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/jeollaPage";
    }
    @GetMapping(value = "/rest/jeolla/region")
    public String JeollaRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/jeollaPage";
    }

    @GetMapping(value = "/rest/gyeongsang")
    public String GyeongsangMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getGyeongsangRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gyeongsangPage";
    }
    @GetMapping(value = "/rest/gyeongsang/region")
    public String GyeongsangRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/gyeongsangPage";
    }

    @GetMapping(value = "/rest/jeju")
    public String JejuMain(RestSearchDto restSearchDto, @RequestParam(required = false)List<String> regions,Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getJejuRestPage(regions,pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/jejuPage";
    }
    @GetMapping(value = "/rest/jeju/region")
    public String JejuRegion(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);
        Page<MainRestDto> rests;

        if(restSearchDto.getRegion() != null && !restSearchDto.getRegion().isEmpty()) {
            rests = restService.getRegionRestPage(restSearchDto.getRegion(), pageable);
        } else {
            rests = restService.getMainRestPage(restSearchDto, pageable);
        }

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/jejuPage";
    }

    @GetMapping(value = "/rest/namdong")
    public String NamdongMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        // 서울로 시작하는 주소의 음식점만 가져오는 로직으로 변경
        Page<MainRestDto> rests = restService.getNamdongRestPage(pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto); // 이 부분은 검색 기능이 필요하면 그대로 두고, 필요 없다면 제거 가능
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/namdongPage";
    }

    @GetMapping(value = "/rest/namdong/busan")
    public String BusanMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getBusanRestPage(pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto); // 이 부분은 검색 기능이 필요하면 그대로 두고, 필요 없다면 제거 가능
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/namdongPage";
    }

    @GetMapping(value = "/rest/namdong/ulsan")
    public String UlsanMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getUlsanRestPage(pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto); // 이 부분은 검색 기능이 필요하면 그대로 두고, 필요 없다면 제거 가능
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/namdongPage";
    }

    @GetMapping(value = "/rest/namdong/daegu")
    public String DaeguMain(RestSearchDto restSearchDto, Optional<Integer> page, @RequestParam(required = false) Boolean deleteSuccess, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 16);

        Page<MainRestDto> rests = restService.getDaeguRestPage(pageable);

        model.addAttribute("rests", rests);
        model.addAttribute("restSearchDto", restSearchDto); // 이 부분은 검색 기능이 필요하면 그대로 두고, 필요 없다면 제거 가능
        model.addAttribute("maxPage", 5);
        model.addAttribute("deleteSuccess", deleteSuccess);
        return "region/namdongPage";
    }
}
