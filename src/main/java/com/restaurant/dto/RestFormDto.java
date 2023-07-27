package com.restaurant.dto;

import com.restaurant.entity.Rest;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RestFormDto {
    private Long id;

    @NotBlank(message = "식당명은 필수 입력 값입니다.")
    private String restNm;

    @NotBlank(message = "식당 번호는 필수 입력 값입니다.")
    private String restPhone;

    @NotBlank(message = "식당 주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "식당 카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "식당 소개글은 필수 입력 값입니다.")
    private String introduction;

    @NotBlank(message = "식당 설명글은 필수 입력 값입니다.")
    private String restDetail;

    private List<RestImgDto> restImgDtoList = new ArrayList<>();

    private List<Long> restImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Rest createRest(){
        return modelMapper.map(this, Rest.class);
    }

    public static RestFormDto of(Rest rest){
        return modelMapper.map(rest,RestFormDto.class);
    }
}
