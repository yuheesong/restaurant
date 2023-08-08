package com.restaurant.dto;

import com.restaurant.entity.Member;
import com.restaurant.entity.Rest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberFormDto {

    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String identifier;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    /*마이페이지-회원 정보 수정
    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberFormDto of(Member member){
        return modelMapper.map(member,MemberFormDto.class);
    }*/
}
