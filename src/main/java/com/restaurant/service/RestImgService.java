package com.restaurant.service;

import com.restaurant.entity.RestImg;
import com.restaurant.repository.RestImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class RestImgService {
    @Value("${restImgLocation}")
    private String restImgLocation;

    private final RestImgRepository restImgRepository;

    private final FileService fileService;

    public void saveRestImg(RestImg restImg, MultipartFile restImgFile) throws Exception{
        String oriImgName = restImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(restImgLocation, oriImgName,
                    restImgFile.getBytes());
            imgUrl = "/images/rest/" + imgName;
        }

        //상품 이미지 정보 저장
        restImg.updateRestImg(oriImgName, imgName, imgUrl);
        restImgRepository.save(restImg);
    }
}
