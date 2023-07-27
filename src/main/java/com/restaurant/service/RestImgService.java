package com.restaurant.service;

import com.restaurant.entity.RestImg;
import com.restaurant.repository.RestImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

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
    public void updateRestImg(Long restImgId, MultipartFile restImgFile) throws Exception{
        if(!restImgFile.isEmpty()){
            RestImg savedRestImg = restImgRepository.findById(restImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedRestImg.getImgName())) {
                fileService.deleteFile(restImgLocation+"/"+
                        savedRestImg.getImgName());
            }

            String oriImgName = restImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(restImgLocation, oriImgName, restImgFile.getBytes());
            String imgUrl = "/images/rest/" + imgName;
            savedRestImg.updateRestImg(oriImgName, imgName, imgUrl);
        }
    }
}
