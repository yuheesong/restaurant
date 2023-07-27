package com.restaurant.service;

import com.restaurant.dto.RestFormDto;
import com.restaurant.dto.RestImgDto;
import com.restaurant.dto.RestSearchDto;
import com.restaurant.entity.Rest;
import com.restaurant.entity.RestImg;
import com.restaurant.repository.RestImgRepository;
import com.restaurant.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestService {
    private final RestRepository restRepository;

    private final RestImgService restImgService;

    private final RestImgRepository restImgRepository;

    public Long saveRest(RestFormDto restFormDto, List<MultipartFile> restImgFileList) throws Exception{

        //상품 등록
        Rest rest = restFormDto.createRest();
        restRepository.save(rest);

        //이미지 등록
        for(int i=0;i<restImgFileList.size();i++){
            RestImg restImg = new RestImg();
            restImg.setRest(rest);

            if(i == 0)
                restImg.setRepimgYn("Y");
            else
                restImg.setRepimgYn("N");

            restImgService.saveRestImg(restImg, restImgFileList.get(i));
        }

        return rest.getId();
    }
    @Transactional(readOnly = true)
    public RestFormDto getRestDtl(Long restId){
        List<RestImg> restImgList = restImgRepository.findByRestIdOrderByIdAsc(restId);
        List<RestImgDto> restImgDtoList = new ArrayList<>();
        for (RestImg restImg : restImgList) {
            RestImgDto restImgDto = RestImgDto.of(restImg);
            restImgDtoList.add(restImgDto);
        }

        Rest rest = restRepository.findById(restId)
                .orElseThrow(EntityNotFoundException::new);
        RestFormDto restFormDto = RestFormDto.of(rest);
        restFormDto.setRestImgDtoList(restImgDtoList);
        return restFormDto;
    }
    public Long updateRest(RestFormDto restFormDto, List<MultipartFile> restImgFileList) throws Exception{
        //상품 수정
        Rest rest = restRepository.findById(restFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        rest.updateRest(restFormDto);
        List<Long> restImgIds = restFormDto.getRestImgIds();

        //이미지 등록
        for(int i=0;i<restImgFileList.size();i++){
            restImgService.updateRestImg(restImgIds.get(i),
                    restImgFileList.get(i));
        }

        return rest.getId();
    }
    @Transactional(readOnly = true)
    public Page<Rest> getAdminRestPage(RestSearchDto restSearchDto, Pageable pageable){
        return restRepository.getAdminRestPage(restSearchDto, pageable);
    }


}
