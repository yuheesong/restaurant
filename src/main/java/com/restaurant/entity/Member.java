package com.restaurant.entity;

import com.restaurant.constant.Role;
import com.restaurant.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identifier; //회원의 아이디

    private String name; //중요하진 않을것 같다

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setIdentifier(memberFormDto.getIdentifier());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        if(memberFormDto.getIdentifier().startsWith("admin_")) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.USER);
        }
        return member;
    }
    // 마이페이지 - 회원 정보 수정
    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        this.name=memberFormDto.getName();
        //this.identifier=memberFormDto.getIdentifier();
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        this.setPassword(password);
    }
}
