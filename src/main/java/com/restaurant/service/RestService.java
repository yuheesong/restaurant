package com.restaurant.service;

import com.restaurant.dto.RestFormDto;
import com.restaurant.entity.Rest;
import com.restaurant.entity.RestImg;
import com.restaurant.repository.RestImgRepository;
import com.restaurant.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
