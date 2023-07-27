package com.restaurant.dto;

import com.restaurant.entity.RestImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class RestImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static RestImgDto of(RestImg restImg){
        return modelMapper.map(restImg, RestImgDto.class);
    }
}
